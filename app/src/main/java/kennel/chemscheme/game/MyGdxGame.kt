package kennel.chemscheme.game

import android.util.Log
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.VertexAttributes.Usage
import com.badlogic.gdx.graphics.g3d.*
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3
import kennel.chemscheme.BuildConfig
import kennel.chemscheme.positionProcessing.Atom3D
import kennel.chemscheme.positionProcessing.Structure3D
import kennel.chemscheme.positionProcessing.Vector
import kennel.chemscheme.structure.MolStruct
import kotlin.math.*

operator fun Vector3.plus(other : Vector3) = Vector3(x + other.x, y + other.y, z + other.z)
operator fun Vector3.minus(other : Vector3) = Vector3(x - other.x, y - other.y, z - other.z)
operator fun Vector3.times(k : Float) = Vector3(x * k, y * k, z*k)
operator fun Vector3.div(k : Float) = Vector3(x / k, y / k, z/k)
fun Vector3.power(k : Float) = Vector3(
    Math.pow(x.toDouble(), k.toDouble()).toFloat(),
    Math.pow(y.toDouble(), k.toDouble()).toFloat(),
    Math.pow(z.toDouble(), k.toDouble()).toFloat()
)

fun min(a : Vector3, b : Vector3) : Vector3{
    return Vector3(min(a.x, b.x), min(a.y, b.y), min(a.z, b.z))
}

fun max(a : Vector3, b : Vector3) : Vector3{
    return Vector3(max(a.x, b.x), max(a.y, b.y), max(a.z, b.z))
}

enum class VisualizationMode {
    ZHELUD, CLASSIC;
    /*private val vals: Array<VisualizationMode> = values()
    fun next() : VisualizationMode? {
        return vals[(ordinal + 1) % vals.size]
    }*/
}

//МЫ ПИШЕМ ИСТОРИЮ!!!!!!!!!111
class MyGdxGame(val onCreate : (() -> Unit)) : ApplicationAdapter() {

    //Ну все, отдаю вам все свои должные!
    private lateinit var cam: PerspectiveCamera
    private lateinit var batch: ModelBatch
    private lateinit var env: Environment
    private lateinit var camCtrl: CameraInputController
    private val instances = mutableListOf<ModelInstance>()
    private lateinit var modeler: Modeler
    private val funQueue = mutableListOf<() -> Unit>()
    private val argsQueue = mutableListOf<Any>()
    private var lastCamPos = Vector(0.0, 0.0, -1.0)
    private var prevX = 0
    private var prevY = 0
    private var curDeltaTime = 0f
    private val targetDelta = 0.02f
    private val MAX_NUMBER_OF_POINTS = 20

    private var isEditing = false
    private var isRendering = false
    private var visualizationMode : VisualizationMode = VisualizationMode.values()[0]

    companion object {
        val ATOM_PROPERTIES = hashMapOf<MolStruct.Elements, Pair<Float, Color>>(
                MolStruct.Elements.C to Pair(1.0f, Color.BLACK),
                MolStruct.Elements.H to Pair(0.5f, Color.WHITE),
                MolStruct.Elements.I to Pair(1.6f, Color.PURPLE),
                MolStruct.Elements.F to Pair(0.8f, Color.CYAN),
                MolStruct.Elements.Br to Pair(1.2f, Color.BROWN),
                MolStruct.Elements.Cl to Pair(1.0f, Color.GREEN),
                MolStruct.Elements.O to Pair(1.0f, Color.CORAL)
        )
    }

    private object constants {
        val zheludScale = 1.4f
        val classicScale = 0.7f
    }

    override fun create() {

        // Задаем свет
        env = Environment()
        env.set(ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f))
        addLights()

        modeler = Modeler()
        batch = ModelBatch()

        // Создаем камеру
        cam = PerspectiveCamera(67f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        //cam.position.set(1f, 1f, 1f)
        //cam.lookAt(0f, 0f, 0f)
        cam.near = 1f
        cam.far = 300f
        cam.update()

        // А ето, чтобы камеру можно было двигать, но тут есть не все фичи
        // и камеру можно перемещать не по всем направлениям :((
        camCtrl = CameraInputController(cam)
        Gdx.input.inputProcessor = camCtrl
        Log.i("test", "create")
        onCreate.invoke()
    }

    private fun addLights() {
        env.add(DirectionalLight().set(0.8f, 0.8f, 0.8f, 1f, 0f, 0f))

    }

    fun clear(){
        while (isRendering){
            Thread.sleep(5)
        }
        isEditing = true
        instances.clear()
        // Создаем камеру
        cam = PerspectiveCamera(67f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        //cam.position.set(1f, 1f, 1f)
        //cam.lookAt(0f, 0f, 0f)
        cam.near = 1f
        cam.far = 300f
        cam.update()

        // А ето, чтобы камеру можно было двигать, но тут есть не все фичи
        // и камеру можно перемещать не по всем направлениям :((
        camCtrl = CameraInputController(cam)
        Gdx.input.inputProcessor = camCtrl
        isEditing = false
        //funQueue.clear()
        //argsQueue.clear()
    }

    fun changeMode(){
        visualizationMode = when(visualizationMode){
            VisualizationMode.ZHELUD -> VisualizationMode.CLASSIC
            VisualizationMode.CLASSIC -> VisualizationMode.ZHELUD
        }
    }

    private fun addAtomToGraph() {
        // Создаем modelInstance и задаем положение ы пространстве
        val arg = argsQueue.removeAt(0) as Atom3D
        val inst = ModelInstance(modeler.getModelForId(arg.atom.name))
        inst.transform.set(arg.position.toGdx3vec(), Quaternion(0f, 0f, 0f, 0f))

        when(visualizationMode){
            VisualizationMode.ZHELUD -> inst.transform.scale(constants.zheludScale, constants.zheludScale, constants.zheludScale)
            VisualizationMode.CLASSIC -> inst.transform.scale(constants.classicScale, constants.classicScale, constants.classicScale)
        }


        instances.add(inst)
    }

    private fun turnCamera(struct : Structure3D){
        var minPos : Vector3 = Vector3(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE).power(2f)
        var maxPos : Vector3 = Vector3(Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE).power(2f)

        //var middle = Vector3.Zero

        struct.vertices.forEach {
            //middle = middle + it.position.toGdx3vec().power(2f)
            minPos = min(minPos, it.position.toGdx3vec())
            maxPos = max(maxPos, it.position.toGdx3vec())
        }

        var size = 2f * max(maxPos.x - minPos.x, maxPos.y - minPos.y) / 2
        var alpha = 33.5f * Math.PI / 180
        var distance = size * cos(alpha) / sin(alpha)
        cam.position.set(0f,0f, distance.toFloat())

        Log.i("test", "$maxPos")
        Log.i("test", "$minPos")

        val center = (minPos + maxPos) / 2f

        //middle = (middle / (struct.vertices.size.toFloat())).power(0.5f)

        camCtrl.reset()
        cam.lookAt(center)
        camCtrl.target = center
        cam.update()
        //Log.i("test", "middle " + "$middle")
        //Log.i("test", "$center")
    }

    // Тута создаются все модельки, чтение данных из Structure3D (шок, не правда ли)
    fun createFromArray(data: Structure3D) {
        while (isRendering){
            Thread.sleep(5)
        }
        isEditing = true
        turnCamera(data)
        data.vertices.forEach {
            argsQueue.add(it)
            // А тута создаются атомы
            funQueue.add { addAtomToGraph() }
        }
        val connsArr = getConnections(data)
        connsArr.forEach {
            argsQueue.add(it)
            funQueue.add {
                // Берем параметры из очереди аргументов
                if (BuildConfig.DEBUG && !(argsQueue[0] is List<*>)) {
                    error("argsQueue is not list")
                }
                if (BuildConfig.DEBUG && !((argsQueue[0] as List<*>)[0] is Atom3D)){
                    error("argsQueue is not list of Atom3D")
                }

                val gotten = argsQueue.removeAt(0) as List<Atom3D>

                val direction = gotten[0].position - gotten[1].position
                val builder = ModelBuilder()
                val c = builder.createCylinder(0.1f, direction.magnitude().toFloat(), 0.1f, 20,
                    Material(ColorAttribute.createDiffuse(Color.GRAY)), (Usage.Position or Usage.Normal).toLong())
                val cInstance = ModelInstance(c)
                cInstance.transform.setToRotation((direction * Vector(0.0, 1.0, 0.0)).toGdx3vec(), -acos(direction.y / direction.magnitude()).toFloat() * 180f / Math.PI.toFloat())
                cInstance.transform.set(((gotten[0].position + gotten[1].position) / 2.0).toGdx3vec(),
                    cInstance.transform.getRotation(Quaternion()))




                instances.add(cInstance)

                // Создаем линии
                /*val modelBuilder = ModelBuilder()
                modelBuilder.begin()
                val builder = modelBuilder.part("line", 1, 3, Material())
                builder.setColor(Color.RED)
                builder.line(gotten[0].position.toGdx3vec(), gotten[1].position.toGdx3vec())
                val lineModel = modelBuilder.end()
                val lineInstance = ModelInstance(lineModel)
                modeler.sticksModels.add(lineModel)
                instances.add(lineInstance)*/
            }
        }
        isEditing = false
    }

    // Здеся попытка перемещения камеры при ведении двумя пальцами,
    // но ничего не получилося ((
    private fun moveCam(vert: Int, horiz: Int) {
        argsQueue.add(vert)
        argsQueue.add(horiz)
        funQueue.add {
            val vertical = argsQueue.removeFirst() as Int
            val horizontal = argsQueue.removeFirst() as Int
            val movingVec = cam.position.toKennelVec() - lastCamPos
            val lookingDir = cam.direction.toKennelVec()
            val leftRightDir = movingVec * lookingDir
            Log.i("CamGDX", "dir: ${movingVec.x}, ${movingVec.y}, ${movingVec.z}")
            Log.i("CamGDX", "looking: ${lookingDir.x}, ${lookingDir.y}, ${lookingDir.z}")
            cam.position.add((movingVec*vertical).toGdx3vec())
            cam.position.add((leftRightDir*horizontal).toGdx3vec())
        }
    }

    // Тут происходит отслеживание нажатия двух пальцев, чтобы можно было таскать камеру
    // в пространстве и вращать вокруг оси смотрения
    private fun procFingersTouches() {
        var curTouched = 0
        (0 until MAX_NUMBER_OF_POINTS).forEach {
            if (Gdx.input.isTouched(it)) {
                curTouched++
            }
        }
        if (curTouched == 2) {
            Log.i("poooints", "doing")
            val x = Gdx.input.x
            val y = Gdx.input.y
            val ho = when {
                x > prevX + 10 -> 1
                x + 10 < prevX -> -1
                else -> 0
            }
            val ve = when {
                y > prevY + 10 -> 1
                y + 10 < prevY -> -1
                else -> 0
            }
            moveCam(ve, ho)
            prevX = x
            prevY = y
        }

    }


    // Новые модели и инстанции, как у меня получилось, можно создавать только в функции render,
    // поэтому есть переменные funQueue и funArgs, куда добавляются функции, которые потом исполняются
    // во время рендера
    override fun render() {
        if(isEditing){
            return
        }
        isRendering = true
        funQueue.forEach { it.invoke() }
        funQueue.clear()
        argsQueue.clear()

        Gdx.gl.glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        batch.begin(cam)
        instances.forEach { batch.render(it, env) }
        camCtrl.update()
        batch.end()
        isRendering = false
    }

    override fun dispose() {
        modeler.atomsModels.forEach { it.model.dispose() }
        modeler.sticksModels.forEach { it.dispose() }
    }

    class Modeler {
        private val builder: ModelBuilder = ModelBuilder()
        val atomsModels: List<AtomModel>
        val sticksModels = mutableListOf<Model>()
        private val defModel: Model

        // Тут создаются модельки атомов, наверное, это можно сделать лучше, но мне лень. (точка).
        init {
            defModel = builder.createSphere(1f, 1f, 1f, 50, 50,
                    Material(ColorAttribute.createDiffuse(Color.GREEN)),
                    (Usage.Position or Usage.Normal).toLong())

            atomsModels = mutableListOf()

            ATOM_PROPERTIES.forEach{
                val am = AtomModel(builder.createSphere(it.value.first, it.value.first, it.value.first, 50, 50,
                        Material(ColorAttribute.createDiffuse(it.value.second)),
                        (Usage.Position or Usage.Normal).toLong()),
                        it.key)
                atomsModels += am
            }
        }

        fun getModelForId(requestedId: MolStruct.Elements): Model {
            atomsModels.forEach {
                if (it.id == requestedId) {
                    return@getModelForId it.model
                }
            }
            return defModel
        }

        data class AtomModel(
                val model: Model,
                val id: MolStruct.Elements,
        )
    }

    // Получить соединения всех атомов друг с другом - возврщается список (атом, атом) соединеий
    private fun getConnections(atoms: Structure3D): List<List<Atom3D>> {
        val res = mutableListOf<List<Atom3D>>()

        atoms.vertices.forEach {
            val first = it
            it.atom.links.forEach { secondId ->
                val conn = listOf(first, atoms.vertices[secondId])
                var exists = false
                res.forEach { inRes ->
                    if (inRes.kennelEquals(conn)) exists = true
                }
                if (!exists && first != atoms.vertices[secondId]) {
                    res.add(conn)
                }
            }
        }
        res.forEach {
            Log.i("sssmmtthh", "${it[0].position} ---> ${it[1].position}")
        }
        return res.toList()
    }


    // Тут всякие утилиты для структур - доп функции конвертации одного в другое
    fun List<Atom3D>.kennelEquals(other: List<Atom3D>): Boolean {
        return this == other || listOf(this[1], this[0]) == other
    }

    fun Vector.toGdx3vec(): Vector3 {
        return Vector3(x.toFloat(), y.toFloat(), z.toFloat())
    }

    fun Vector3.toKennelVec(): Vector {
        return Vector(this.x.toDouble(), this.y.toDouble(), this.z.toDouble())
    }

    operator fun Vector.times(r: Int): Vector {
        return Vector(this.x*r, this.y*r, this.z*r)
    }
}
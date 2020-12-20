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
import com.badlogic.gdx.math.collision.BoundingBox
import kennel.chemscheme.positionProcessing.Atom3D
import kennel.chemscheme.positionProcessing.Structure3D
import kennel.chemscheme.positionProcessing.Vector
import kennel.chemscheme.structure.MolStruct
import kotlin.math.*


//МЫ ПИШЕМ ИСТОРИЮ!!!!!!!!!111
class MyGdxGame : ApplicationAdapter() {
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

    override fun create() {
        // Задаем свет
        env = Environment()
        env.set(ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 3f))
        addLights()

        modeler = Modeler()
        batch = ModelBatch()

        // Создаем камеру
        cam = PerspectiveCamera(67f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        cam.position.set(10f, 10f, 10f)
        cam.lookAt(0f, 0f, 0f)
        cam.near = 1f
        cam.far = 300f
        cam.update()

        // А ето, чтобы камеру можно было двигать, но тут есть не все фичи
        // и камеру можно перемещать не по всем направлениям :((
        camCtrl = CameraInputController(cam)
        Gdx.input.inputProcessor = camCtrl
    }

    private fun addLights() {
        env.add(DirectionalLight().set(0.8f, 0.8f, 0.8f, 1f, 0f, 0f))

    }

    private fun addAtomToGraph() {
        // Создаем modelInstance и задаем положение ы пространстве
        val arg = argsQueue.removeAt(0) as Atom3D
        val inst = ModelInstance(modeler.getModelForId(arg.atom.name))
        inst.transform.set(arg.position.toGdx3vec(), Quaternion(0f, 0f, 0f, 0f))

        instances.add(inst)
    }

    // Тута создаются все модельки, чтение данных из Structure3D (шок, не правда ли)
    fun createFromArray(data: Structure3D) {
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
                val gotten = argsQueue.removeAt(0) as List<Atom3D>
                // Создаем линии
                val modelBuilder = ModelBuilder()
                modelBuilder.begin()
                val builder = modelBuilder.part("line", 1, 3, Material())
                builder.setColor(Color.RED)
                builder.line(gotten[0].position.toGdx3vec(), gotten[1].position.toGdx3vec())
                val lineModel = modelBuilder.end()
                val lineInstance = ModelInstance(lineModel)
                modeler.sticksModels.add(lineModel)
                instances.add(lineInstance)
            }
        }
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
        curDeltaTime += Gdx.graphics.deltaTime

        if (curDeltaTime >= targetDelta) {
            procFingersTouches()
            curDeltaTime -= targetDelta
        }


        funQueue.forEach { it.invoke() }
        funQueue.clear()
        argsQueue.clear()

        lastCamPos = cam.position.toKennelVec()
        camCtrl.update()

        Gdx.gl.glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        batch.begin(cam)
        instances.forEach { batch.render(it, env) }
        batch.end()
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

            atomsModels = listOf(
                    AtomModel(
                            builder.createSphere(1f, 1f, 1f, 50, 50,
                                    Material(ColorAttribute.createDiffuse(Color.BLUE)),
                                    (Usage.Position or Usage.Normal).toLong()),
                            MolStruct.Elements.C),
                    AtomModel(
                            builder.createSphere(1f, 1f, 1f, 50, 50,
                                    Material(ColorAttribute.createDiffuse(Color.RED)),
                                    (Usage.Position or Usage.Normal).toLong()),
                            MolStruct.Elements.Cl),
                    AtomModel(
                            builder.createSphere(1f, 1f, 1f, 50, 50,
                                    Material(ColorAttribute.createDiffuse(Color.BROWN)),
                                    (Usage.Position or Usage.Normal).toLong()),
                            MolStruct.Elements.Br),
                    AtomModel(
                            builder.createSphere(1f, 1f, 1f, 50, 50,
                                    Material(ColorAttribute.createDiffuse(Color.CORAL)),
                                    (Usage.Position or Usage.Normal).toLong()),
                            MolStruct.Elements.H),
                    AtomModel(
                            builder.createSphere(1f, 1f, 1f, 50, 50,
                                    Material(ColorAttribute.createDiffuse(Color.CYAN)),
                                    (Usage.Position or Usage.Normal).toLong()),
                            MolStruct.Elements.F),
                    AtomModel(
                            builder.createSphere(1f, 1f, 1f, 50, 50,
                                    Material(ColorAttribute.createDiffuse(Color.GOLD)),
                                    (Usage.Position or Usage.Normal).toLong()),
                            MolStruct.Elements.I)
            )
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
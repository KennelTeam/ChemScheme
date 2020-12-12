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
import kennel.chemscheme.positionProcessing.Atom3D
import kennel.chemscheme.positionProcessing.Structure3D
import kennel.chemscheme.positionProcessing.Vector
import kennel.chemscheme.structure.MolStruct
import kotlin.math.pow
import kotlin.math.sqrt

class MyGdxGame : ApplicationAdapter() {
    private lateinit var cam: PerspectiveCamera
    private lateinit var batch: ModelBatch
    private lateinit var env: Environment
    private lateinit var camCtrl: CameraInputController
    private val atomsInstances = mutableListOf<ModelInstance>()
    private lateinit var modeler: Modeler
    private val funQueue = mutableListOf<() -> Unit>()
    private val argsQueue = mutableListOf<Any>()

    override fun create() {
        env = Environment()
        env.set(ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f))
        addLights()

        modeler = Modeler()

        batch = ModelBatch()

        cam = PerspectiveCamera(67f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        cam.position.set(10f, 10f, 10f)
        cam.lookAt(0f, 0f, 0f)
        cam.near = 1f
        cam.far = 300f
        cam.update()

        camCtrl = CameraInputController(cam)
        Gdx.input.inputProcessor = camCtrl
    }

    private fun addLights() {
        env.add(DirectionalLight().set(0.8f, 0.8f, 0.8f, 1f, 0f, 0f))

    }

    private fun addAtomToGraph() {
        val arg = argsQueue.removeAt(0) as Atom3D
        val inst = ModelInstance(modeler.getModelForId(arg.atom.name))
        inst.transform.set(arg.position.toGdx3vec(), Quaternion(0f, 0f, 0f, 0f))

        atomsInstances.add(inst)
    }

    fun createFromArray(data: Structure3D) {
        data.vertices.forEach {
            argsQueue.add(it)
            funQueue.add { addAtomToGraph() }
        }
        val connsArr = getConnections(data)
        connsArr.forEach {
            argsQueue.add(it)
            funQueue.add {
                val gotten = argsQueue.removeAt(0) as List<Atom3D>
                val len = countLen(gotten)
                val model = modeler.builder.createCylinder(
                    0.1f, len, 0.1f, 50,
                    Material(ColorAttribute.createDiffuse(Color.LIGHT_GRAY)),
                    (Usage.Position or Usage.Normal).toLong()
                )
                modeler.sticksModels.add(model)
                val instance = ModelInstance(model)
                val q = Quaternion(gotten[1].position.toGdx3vec(), 0f)
                instance.transform.setToLookAt(gotten[0].position.toGdx3vec(), gotten[1].position.toGdx3vec())
                instance.transform.translate(gotten[0].position.toGdx3vec())
                atomsInstances.add(instance)
            }
        }
    }

    fun countLen(points: List<Atom3D>): Float {
        return sqrt(
            (points[0].position.x - points[1].position.x).pow(2)+
                    (points[0].position.y - points[1].position.y).pow(2)+
                    (points[0].position.z - points[1].position.z).pow(2)).toFloat()
    }

    override fun render() {
        funQueue.forEach { it.invoke() }
        funQueue.clear()
        argsQueue.clear()

        camCtrl.update()

        Gdx.gl.glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        batch.begin(cam)
        atomsInstances.forEach { batch.render(it, env) }
        batch.end()
    }

    override fun dispose() {
        modeler.atomsModels.forEach { it.model.dispose() }
        modeler.sticksModels.forEach { it.dispose() }
    }

    class Modeler {
        val builder: ModelBuilder
        val atomsModels: List<AtomModel>
        val sticksModels = mutableListOf<Model>()
        private val defModel: Model

        init {
            builder = ModelBuilder()
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

    fun getConnections(atoms: Structure3D): List<List<Atom3D>> {
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

    fun List<Atom3D>.kennelEquals(other: List<Atom3D>): Boolean {
        return this == other || listOf(this[1], this[0]) == other
    }

    fun Vector.toGdx3vec(): Vector3 {
        return Vector3(x.toFloat(), y.toFloat(), z.toFloat())
    }
}
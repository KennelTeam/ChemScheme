package kennel.chemscheme.game

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
import com.badlogic.gdx.math.Vector3
import kennel.chemscheme.positionProcessing.Structure3D
import kennel.chemscheme.positionProcessing.Vector

class MyGdxGame : ApplicationAdapter() {
    private lateinit var cam: PerspectiveCamera
    private lateinit var batch: ModelBatch
    private lateinit var env: Environment
    private lateinit var camCtrl: CameraInputController
    private val atomsModels = mutableListOf<Model>()
    private val atomsInstances = mutableListOf<ModelInstance>()
    private val builder = ModelBuilder()

    override fun create() {
        env = Environment()
        env.set(ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f))
        env.add(DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f))
        env.add(DirectionalLight().set(0.8f, 0.8f, 0.8f, 1f, 0.8f, 0.2f))
//        val pl = PointLight()
//        pl.set(0.8f, 0.8f, 0.8f, 10f, 10f, 10f, f)
//        env.add(pl)

        batch = ModelBatch()

        cam = PerspectiveCamera(67f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        cam.position.set(10f, 10f, 10f)
        cam.lookAt(0f, 0f, 0f)
        cam.near = 1f
        cam.far = 300f
        cam.update()

        camCtrl = CameraInputController(cam)
        Gdx.input.inputProcessor = camCtrl


//        val b = MeshBuilder()
//        b.begin((Usage.Position or Usage.Normal).toLong())
//        CylinderShapeBuilder.build(b, 10f, 10f, 10f, 3)
//        val mesh = b.end()
//
//
//        builder.begin()
//        builder.part("mesh", mesh, GL20.GL_TRIANGLES, Material(ColorAttribute.createDiffuse(Color.GREEN)))
//        model = builder.end()

    }

    fun createFromArray(struct: Structure3D) {
        val verts = struct.vertices
       // verts.forEach {
            val model = builder.createSphere(1f, 1f, 1f, 50, 50,
                Material(ColorAttribute.createDiffuse(Color.GREEN)),
                (Usage.Position or Usage.Normal).toLong())

            val instance = ModelInstance(model)
            atomsModels.add(model)
            atomsInstances.add(instance)
        //}
    }

    override fun render() {
        camCtrl.update()

        Gdx.gl.glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        batch.begin(cam)
        atomsInstances.forEach { batch.render(it, env) }
        batch.end()
    }

    override fun dispose() {
        atomsModels.forEach { it.dispose() }
    }
}

fun Vector.toGdx3vec(): Vector3 {
    return Vector3(x.toFloat(), y.toFloat(), z.toFloat())
}
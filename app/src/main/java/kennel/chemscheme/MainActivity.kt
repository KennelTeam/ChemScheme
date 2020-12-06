package kennel.chemscheme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidFragmentApplication

import kennel.chemscheme.positionProcessing.*
import kennel.chemscheme.structure.MolStruct

class MainActivity : AppCompatActivity(), AndroidFragmentApplication.Callbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val lbf = MolGdxFrag()
//        supportFragmentManager.beginTransaction().add(R.id.frag3dLayout, lbf).commit()
//        try {
//            test3d()
//            Log.i("test3d", "test passed")
//        } catch(e: Exception) {
//            Log.i("test3d", "test failed")
//        }
    }

    fun test3d(){
        var struct : MolStruct.Structure = MolStruct.Structure()
        struct.add(MolStruct.Elements.C, -1, 0)
        struct.add(MolStruct.Elements.Br, 0, 0)
        struct.add(MolStruct.Elements.H, 0, 1)
        struct.add(MolStruct.Elements.Cl, 0, 2)

        struct.add(MolStruct.Elements.C, 0, 3)

        struct.add(MolStruct.Elements.C, 4, 1)

        struct.add(MolStruct.Elements.F, 5, 1)
        struct.add(MolStruct.Elements.Br, 5, 2)
        struct.add(MolStruct.Elements.I, 5, 3)

        struct.add(MolStruct.Elements.I, 4, 2)
        struct.add(MolStruct.Elements.C, 4, 3)

        struct.add(MolStruct.Elements.F, 10, 1)
        struct.add(MolStruct.Elements.Br, 10, 2)
        struct.add(MolStruct.Elements.I, 10, 3)

        var struct3d = PositionCalculator.calculatePositions(struct)
        struct3d.show()
    }

    override fun exit() {
        TODO("Not yet implemented")
    }
}
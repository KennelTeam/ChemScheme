package kennel.chemscheme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import kennel.chemscheme.positionProcessing.*
import kennel.chemscheme.structure.MolStruct

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        test3d()

    }

    fun test3d(){
        var struct : MolStruct.Structure = MolStruct.Structure()
        struct.add(MolStruct.Elements.C, -1, 0)
        struct.add(MolStruct.Elements.Br, 0, 0)
        struct.add(MolStruct.Elements.H, 0, 1)
        struct.add(MolStruct.Elements.Cl, 0, 2)
        struct.add(MolStruct.Elements.F, 0, 3)

        var struct3d = PositionCalculator.calculatePositions(struct)
        struct3d.show()
    }
}
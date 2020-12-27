package kennel.chemscheme.mol3d

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import kennel.chemscheme.game.MyGdxGame
import kennel.chemscheme.positionProcessing.Atom3D
import kennel.chemscheme.positionProcessing.PositionCalculator
import kennel.chemscheme.positionProcessing.Structure3D
import kennel.chemscheme.structure.MolStruct

class MolGdxFrag: AndroidFragmentApplication() {
    //
    // Ето класс, который создает окошко libgdx в качестве фрагмента
    //
    val gdxGraph = MyGdxGame({onGdxInitialized()})

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val i = initializeForView(gdxGraph)

        return i
    }

    fun onGdxInitialized(){
        gdxGraph.createFromArray(test3d())
    }


    fun test3d(): Structure3D{
        val struct : MolStruct.Structure = MolStruct.Structure()
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

        struct.add(MolStruct.Elements.C, 10, 1)
        struct.add(MolStruct.Elements.Br, 10, 2)
        struct.add(MolStruct.Elements.I, 10, 3)

        struct.add(MolStruct.Elements.C, 11, 1)
        struct.add(MolStruct.Elements.H, 11, 2)
        struct.add(MolStruct.Elements.C, 11, 3)

        struct.add(MolStruct.Elements.F, 14, 1)
        struct.add(MolStruct.Elements.H, 14, 2)
        struct.add(MolStruct.Elements.H, 14, 3)

        struct.add(MolStruct.Elements.I, 16, 1)
        struct.add(MolStruct.Elements.H, 16, 2)
        struct.add(MolStruct.Elements.H, 16, 3)

        return PositionCalculator.calculatePositions(struct)
    }
}

//Structure3D(mutableListOf(Atom3D(MolStruct.Atom(MolStruct.Elements.C, arrayOf(10)))))
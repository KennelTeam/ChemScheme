package kennel.chemscheme.mol3d

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import kennel.chemscheme.game.MyGdxGame
import kennel.chemscheme.positionProcessing.PositionCalculator
import kennel.chemscheme.positionProcessing.Structure3D
import kennel.chemscheme.structure.AtomType
import kennel.chemscheme.structure.GraphStruct

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
      //  gdxGraph.createFromArray(test3d())
    }

    fun resetMode(){
        gdxGraph.clear()
        gdxGraph.changeMode()
        gdxGraph.createFromArray(test3d())
    }

    fun test3d(): Structure3D{
        val struct = GraphStruct()

        struct.add(AtomType.Bromium, struct.allAtoms[0], 0)
        struct.add(AtomType.Hydrogen, struct.allAtoms[0], 1)
        struct.add(AtomType.Chlorum, struct.allAtoms[0], 2)

        struct.add(AtomType.Carbon, struct.allAtoms[0], 3)

        struct.add(AtomType.Carbon, struct.allAtoms[4], 1)

        struct.add(AtomType.Fluorine, struct.allAtoms[5], 1)
        struct.add(AtomType.Bromium, struct.allAtoms[5], 2)
        struct.add(AtomType.Iodine, struct.allAtoms[5], 3)

        struct.add(AtomType.Iodine, struct.allAtoms[4], 2)
        struct.add(AtomType.Carbon, struct.allAtoms[4], 3)

        struct.add(AtomType.Carbon, struct.allAtoms[10], 1)
        struct.add(AtomType.Bromium, struct.allAtoms[10], 2)
        struct.add(AtomType.Iodine, struct.allAtoms[10], 3)

        struct.add(AtomType.Carbon, struct.allAtoms[11], 1)
        struct.add(AtomType.Hydrogen, struct.allAtoms[11], 2)
        struct.add(AtomType.Carbon, struct.allAtoms[11], 3)

        struct.add(AtomType.Fluorine, struct.allAtoms[14], 1)
        struct.add(AtomType.Hydrogen, struct.allAtoms[14], 2)
        struct.add(AtomType.Hydrogen, struct.allAtoms[14], 3)

        struct.add(AtomType.Iodine, struct.allAtoms[16], 1)
        struct.add(AtomType.Hydrogen, struct.allAtoms[16], 2)
        struct.add(AtomType.Hydrogen, struct.allAtoms[16], 3)

        return PositionCalculator.calculatePositions(struct)
    }
}
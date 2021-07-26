package kennel.chemscheme.screens.view_3d

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import kennel.chemscheme.game.Libgdx3D
import kennel.chemscheme.positionProcessing.Atom3D
import kennel.chemscheme.positionProcessing.PositionCalculator
import kennel.chemscheme.structure.AtomType.*
import kennel.chemscheme.structure.GraphStruct
import kennel.chemscheme.structure.StructuralAtom

class MolGdxFrag: AndroidFragmentApplication() {
    //
    // Ето класс, который создает окошко libgdx в качестве фрагмента
    //
    val gdxGraph = Libgdx3D({onGdxInitialized()})

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val i = initializeForView(gdxGraph)
        return i
    }

    fun onGdxInitialized(){
      //  gdxGraph.createFromArray(test3d())
    }

    /**
     * zaSHITa uspeh
     */

    fun resetMode(){
        gdxGraph.clear()
        gdxGraph.changeMode()
        gdxGraph.createFromArray(test3d())
    }

    fun test3d(): List<Atom3D> {
        val struct = GraphStruct()

        struct.add(Bromium, struct.allAtoms[0], 0)
        struct.add(Hydrogen, struct.allAtoms[0], 1)
        struct.add(Chlorum, struct.allAtoms[0], 2)

        struct.add(Carbon, struct.allAtoms[0], 3)

        struct.add(Carbon, struct.allAtoms[4], 1)

        struct.add(Fluorine, struct.allAtoms[5], 1)
        struct.add(Bromium, struct.allAtoms[5], 2)
        struct.add(Iodine, struct.allAtoms[5], 3)

        struct.add(Iodine, struct.allAtoms[4], 2)
        struct.add(Carbon, struct.allAtoms[4], 3)

        struct.add(Carbon, struct.allAtoms[10], 1)
        struct.add(Bromium, struct.allAtoms[10], 2)
        struct.add(Iodine, struct.allAtoms[10], 3)

        struct.add(Carbon, struct.allAtoms[11], 1)
        struct.add(Hydrogen, struct.allAtoms[11], 2)
        struct.add(Carbon, struct.allAtoms[11], 3)

        struct.add(Fluorine, struct.allAtoms[14], 1)
        struct.add(Hydrogen, struct.allAtoms[14], 2)
        struct.add(Hydrogen, struct.allAtoms[14], 3)

        struct.add(Iodine, struct.allAtoms[16], 1)
        struct.add(Hydrogen, struct.allAtoms[16], 2)
        struct.add(Hydrogen, struct.allAtoms[16], 3)

        return PositionCalculator.calculatePositions(struct.toTyped { originList ->
            val mainList = originList.map { Atom3D(it) }

            mainList.forEachIndexed { index, atom3D ->
                originList[index].links.map { it.atom.id }
                    .forEach { targetId ->
                        atom3D.addChild(
                            mainList.find { it.id == targetId } as StructuralAtom
                        )
                    }
            }

            return@toTyped mainList
        })
    }
}
package kennel.chemscheme.mol3d

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import kennel.chemscheme.game.MyGdxGame
import kennel.chemscheme.positionProcessing.Atom3D
import kennel.chemscheme.positionProcessing.Structure3D
import kennel.chemscheme.structure.MolStruct

class MolGdxFrag: AndroidFragmentApplication() {
    val gdxGraph = MyGdxGame()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //gdxGraph.createFromArray()
        return initializeForView(gdxGraph)
    }
}

//Structure3D(mutableListOf(Atom3D(MolStruct.Atom(MolStruct.Elements.C, arrayOf(10)))))
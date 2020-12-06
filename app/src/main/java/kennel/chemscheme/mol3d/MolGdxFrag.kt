package kennel.chemscheme.mol3d

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import kennel.chemscheme.game.MyGdxGame

class MolGdxFrag: AndroidFragmentApplication() {
    val gdxGraph = MyGdxGame()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return initializeForView(gdxGraph)
    }
}
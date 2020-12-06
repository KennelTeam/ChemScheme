package kennel.chemscheme.mol3d

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import kennel.chemscheme.R
import kennel.chemscheme.databinding.Mol3dFragmentBinding
import kennel.chemscheme.game.MyGdxGame

class Mol3dFragment: Fragment() {
    private lateinit var binding: Mol3dFragmentBinding
    private lateinit var gdxView: MolGdxFrag
    private lateinit var gdx: MyGdxGame
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.mol3d_fragment,
            container,
            false
        )
        gdxView = MolGdxFrag()
        childFragmentManager.beginTransaction().add(R.id.frag3dLayout, gdxView).commit()

        return binding.root
    }
}
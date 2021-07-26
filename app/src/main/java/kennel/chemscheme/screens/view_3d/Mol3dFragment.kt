package kennel.chemscheme.screens.view_3d

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import kennel.chemscheme.R
import kennel.chemscheme.databinding.Mol3dFragmentBinding
import kennel.chemscheme.game.Libgdx3D

class Mol3dFragment: Fragment() {
    private lateinit var binding: Mol3dFragmentBinding
    private lateinit var gdxView: MolGdxFrag
    private lateinit var gdx: Libgdx3D

    //
    // Класс фрагмента приложения, в котором проимзодит три-дэ
    //
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.mol_3d_fragment,
            container,
            false
        )
        gdxView = MolGdxFrag()
        gdx = gdxView.gdxGraph
//        childFragmentManager.beginTransaction().add(R.id.mol3dFragment, gdxView).commit()

        return binding.root
    }
}
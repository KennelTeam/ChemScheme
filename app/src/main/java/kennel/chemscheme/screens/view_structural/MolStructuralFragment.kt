package kennel.chemscheme.screens.view_structural

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import kennel.chemscheme.R
import kennel.chemscheme.databinding.MolFisherFragmentBinding
import kennel.chemscheme.databinding.MolSkeletonFragmentBinding
import kennel.chemscheme.databinding.MolStructuralFragmentBinding

class MolStructuralFragment: Fragment() {
    private lateinit var binding: MolStructuralFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.mol_structural_fragment,
            container,
            false
        )

        return binding.root
    }
}
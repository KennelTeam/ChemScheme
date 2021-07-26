package kennel.chemscheme.screens.view_fisher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import kennel.chemscheme.R
import kennel.chemscheme.databinding.MolFisherFragmentBinding

class MolFisherFragment: Fragment() {
    private lateinit var binding: MolFisherFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.mol_fisher_fragment,
            container,
            false
        )

        return binding.root
    }
}
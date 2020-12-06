package kennel.chemscheme.structural_formula

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kennel.chemscheme.R
import kennel.chemscheme.databinding.StructuralFormulaFragmentBinding

class StructuralFormulaFragment : Fragment() {

    private lateinit var binding: StructuralFormulaFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.structural_formula_fragment,
            container,
            false
        )

        return binding.root
    }
}
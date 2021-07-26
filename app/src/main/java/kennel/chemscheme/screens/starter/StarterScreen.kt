package kennel.chemscheme.screens.starter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kennel.chemscheme.R
import kennel.chemscheme.databinding.MolFisherFragmentBinding
import kennel.chemscheme.databinding.StarterScreenBinding

class StarterScreen: Fragment() {
    private lateinit var binding: StarterScreenBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.starter_screen,
            container,
            false
        )

        binding.newMolBtn.setOnClickListener {
            this.findNavController().navigate(R.id.action_starterScreen_to_mainScreen)
        }

        return binding.root
    }
}
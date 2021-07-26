package kennel.chemscheme.screens.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import kennel.chemscheme.R
import kennel.chemscheme.databinding.MainScreenBinding
import kennel.chemscheme.databinding.MolFisherFragmentBinding

class MainScreen: Fragment() {
    private lateinit var binding: MainScreenBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.main_screen,
            container,
            false
        )

        return binding.root
    }
}
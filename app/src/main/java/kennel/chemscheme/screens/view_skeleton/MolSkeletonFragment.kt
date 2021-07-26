package kennel.chemscheme.screens.view_skeleton

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import kennel.chemscheme.R
import kennel.chemscheme.databinding.MolSkeletonFragmentBinding

class MolSkeletonFragment: Fragment() {
    private lateinit var binding: MolSkeletonFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.mol_skeleton_fragment,
            container,
            false
        )

        val atom = layoutInflater.inflate(R.layout.skeleton_atom_tmpl, null)
        binding.mainlayout.addView(atom)
        atom.x = 100f
        atom.y = 100f
        atom.findViewById<ImageButton>(R.id.edit_icon).visibility = View.GONE

        val atom2 = layoutInflater.inflate(R.layout.skeleton_atom_tmpl, null)
        binding.mainlayout.addView(atom2)
        atom2.x = 200f
        atom2.y = 200f
        atom2.findViewById<ImageButton>(R.id.edit_icon).visibility = View.GONE


        val connector = layoutInflater.inflate(R.layout.skeleton_connector_tmpl, null)
        binding.mainlayout.addView(connector)
        connector.x = 100f
        connector.y = 100f

        return binding.root
    }
}
package kennel.chemscheme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidFragmentApplication

import kennel.chemscheme.positionProcessing.*
import kennel.chemscheme.structure.MolStruct
import com.google.android.material.navigation.NavigationView
import android.view.MenuItem
import android.util.Log
import android.view.Menu
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kennel.chemscheme.mol3d.Mol3dFragment
import kennel.chemscheme.mol3d.MolGdxFrag
import kennel.chemscheme.structural_formula.StructuralFormulaFragment

class MainActivity : AppCompatActivity(), AndroidFragmentApplication.Callbacks {

    private val startFragment = Mol3dFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener { item: MenuItem -> onNavigationItemSelected(item) }
        changeFragment(startFragment)
    }

    private fun onNavigationItemSelected(item: MenuItem): Boolean {
        var fragment: Fragment? = null
        when (item.itemId) {
            R.id.nav_2d -> fragment = StructuralFormulaFragment()
            R.id.nav_3d -> fragment = Mol3dFragment()
            R.id.nav_exit -> finish()
        }
        if (fragment != null) {
            changeFragment(fragment)
        }

        return true
    }

    private fun changeFragment(fragment: Fragment) {
        if (fragment != null) {
            val fragManager: FragmentManager = getSupportFragmentManager()
            fragManager.beginTransaction().replace(R.id.container, fragment).commit()
            setTitle(fragment.id.toString())
            var drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
            drawer.closeDrawer(GravityCompat.START)
        }
    }

    override fun exit() {
        TODO("Это вроде как надо для норм работы libgdx, не удаляйте функцию пжпж")
    }
}
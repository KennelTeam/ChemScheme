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
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener { item: MenuItem -> onNavigationItemSelected(item) }
        changeFragment(startFragment)
//        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        appBarConfiguration = AppBarConfiguration(setOf(
//            R.id.nav_2d, R.id.nav_3d, R.id.nav_2d), drawerLayout)
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
//        val lbf = MolGdxFrag()
//        supportFragmentManager.beginTransaction().add(R.id.frag3dLayout, lbf).commit()
//        try {
//            test3d()
//            Log.i("test3d", "test passed")
//        } catch(e: Exception) {
//            Log.i("test3d", "test failed")
//        }
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

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }

//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment)
//        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
//    }

    fun test3d(){
        var struct : MolStruct.Structure = MolStruct.Structure()
        struct.add(MolStruct.Elements.C, -1, 0)
        struct.add(MolStruct.Elements.Br, 0, 0)
        struct.add(MolStruct.Elements.H, 0, 1)
        struct.add(MolStruct.Elements.Cl, 0, 2)

        struct.add(MolStruct.Elements.C, 0, 3)

        struct.add(MolStruct.Elements.C, 4, 1)

        struct.add(MolStruct.Elements.F, 5, 1)
        struct.add(MolStruct.Elements.Br, 5, 2)
        struct.add(MolStruct.Elements.I, 5, 3)

        struct.add(MolStruct.Elements.I, 4, 2)
        struct.add(MolStruct.Elements.C, 4, 3)

        struct.add(MolStruct.Elements.F, 10, 1)
        struct.add(MolStruct.Elements.Br, 10, 2)
        struct.add(MolStruct.Elements.I, 10, 3)

        var struct3d = PositionCalculator.calculatePositions(struct)
        struct3d.show()
    }

    override fun exit() {
        TODO("Not yet implemented")
    }
}
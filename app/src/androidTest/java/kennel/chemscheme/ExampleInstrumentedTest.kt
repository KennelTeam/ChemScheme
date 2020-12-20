package kennel.chemscheme

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import kennel.chemscheme.structure.AtomType
import kennel.chemscheme.structure.ConnSight
import kennel.chemscheme.structure.MolStruct
import kennel.chemscheme.structure.SightsAtom

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class TestAvailableSights {
    @Test
    fun test1() {
        val struct = MolStruct()
        struct.add(AtomType.Carbon, struct.allAtoms[0], sightParent =  ConnSight.NorthWest, sightNewAtom = ConnSight.NorthEast)
        val satom = SightsAtom(struct.allAtoms[1])
        assertEquals(satom.availableSights.toSet(), setOf(ConnSight.NorthWest, ConnSight.South))
    }
}
package kennel.chemscheme

import kennel.chemscheme.structure.AtomType
import kennel.chemscheme.structure.ConnSight
import kennel.chemscheme.structure.MolStruct
import kennel.chemscheme.structure.SightsAtom
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TestAvailableSights {
    @Test
    fun test1() {
        val struct = MolStruct()
        struct.add(AtomType.Carbon, struct.allAtoms[0], sightParent =  ConnSight.NorthWest, sightNewAtom = ConnSight.NorthEast)
        val satom = SightsAtom(struct.allAtoms[1])
        assertEquals(setOf(ConnSight.NorthWest, ConnSight.South), satom.availableSights.toSet())

        open class Pes {
            protected fun example() {
                println("Ti pes")
            }
        }

        class Nikita: Pes() {

        }

        class AZh: Pes() {

        }
    }
}
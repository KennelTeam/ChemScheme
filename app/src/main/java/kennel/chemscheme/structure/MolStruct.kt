package kennel.chemscheme.structure

import kotlinx.serialization.json.*
import java.io.*

class MolStruct {
    private val _allAtoms = mutableListOf<BaseAtom>()
    val allAtoms: List<BaseAtom>
        get() = _allAtoms

    fun add(
        type: AtomType,
        connection: SightsAtom,
        sightNewAtom: ConnSight,
        sightParent: ConnSight
    ) {
        if (sightParent in connection.availableSights) {
            val atom = BaseAtom(type, connection, sightNewAtom)
            connection.addNeighbour(atom, sightParent)
            _allAtoms.add(atom)
        }
    }

    fun remove(atom: BaseAtom) {
        atom.links.forEach {
            it.atom.removeNighbour(atom)
        }
        _allAtoms.remove(atom)
    }
}
// Kolya PES

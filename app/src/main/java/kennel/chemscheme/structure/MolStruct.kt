package kennel.chemscheme.structure

import android.content.Context
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*
import java.io.*

class MolStruct {
    private val _allAtoms = mutableListOf<BaseAtom>()
    val allAtoms: List<BaseAtom>
        get() = _allAtoms

   init {
       _allAtoms.add(BaseAtom(AtomType.Carbon))
   }

    fun add(
        type: AtomType,
        connection: BaseAtom,
        sightNewAtom: ConnSight,
        sightParent: ConnSight
    ) {
        if (sightParent in SightsAtom(connection).availableSights) {
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

    fun getByType(type: AtomType): List<BaseAtom> {
        val res = mutableListOf<BaseAtom>()
        allAtoms.forEach {
            if (it.type == type) {
                res.add(it)
            }
        }
        return res
    }

    companion object {
        fun loadFromFile(context: Context, path: String): BaseAtom {
            return Json.decodeFromString(File(context.filesDir, path).readText())
        }
    }
}
// Kolya PES

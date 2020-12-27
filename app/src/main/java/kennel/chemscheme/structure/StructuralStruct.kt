package kennel.chemscheme.structure

@Suppress("UNCHECKED_CAST")
class StructuralStruct<T: StructuralAtom>(startAtomType: AtomType = AtomType.Carbon) {
    private val _allAtoms = mutableListOf<T>()
    val allAtoms: List<T>
        get() = _allAtoms

   init {
       _allAtoms.add(StructuralAtom(IdGenerator.genNewId(), startAtomType, false) as T)
   }

    fun add(
        newAtom: T,
        parent: T,
        vararg args: Any
    ) {
        parent.addChild(newAtom)
        _allAtoms.add()
    }

    fun remove(id: Int) {
        _allAtoms.forEach {
            if (it.id == id) {
                it.structuralParent!!.removeChild(id)
                _allAtoms.remove(it)
            }
        }
    }

    fun replace(idToReplace: Int, newType: AtomType) {
        _allAtoms.forEach {
            if (it.id == idToReplace) {
                it.type = newType
            }
        }
    }
}
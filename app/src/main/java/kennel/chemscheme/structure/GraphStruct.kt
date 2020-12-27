package kennel.chemscheme.structure

class GraphStruct(startAtomType: AtomType = AtomType.Carbon) {
    private val _allAtoms = mutableListOf<Sights2DAtom>()
    val allAtoms: List<Sights2DAtom>
        get() = _allAtoms

   init {
       _allAtoms.add(Sights2DAtom(IdGenerator.genNewId(), startAtomType, false))
   }

    fun add(
        type: AtomType,
        parent: Sights2DAtom,
        sight: Int
    ) {
        val newAtom = Sights2DAtom(IdGenerator.genNewId(), type)
        parent.addChild(newAtom, sight)
        _allAtoms.add(newAtom)
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

    fun <T: StructuralAtom> toTyped(instruction: (StructuralAtom) -> T): List<T> {
        return _allAtoms.map(instruction)
    }
}
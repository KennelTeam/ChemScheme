package kennel.chemscheme.structure

class GraphStruct(startAtomType: AtomType = AtomType.Carbon) {
    private val _allAtoms = mutableListOf<Sides2DAtom>()
    val allAtoms: List<Sides2DAtom>
        get() = _allAtoms

   init {
       _allAtoms.add(Sides2DAtom(IdGenerator.genNewId(), startAtomType, false))
   }

    fun add(
        type: AtomType,
        parent: Sides2DAtom,
        sight: Int
    ) {
        val newAtom = Sides2DAtom(IdGenerator.genNewId(), type, parent = parent)
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

    fun <T: StructuralAtom> toTyped(instructions: (atoms: List<Sides2DAtom>) -> List<T>): List<T> {
        return instructions(allAtoms)
    }

}

fun dslGraph(lambda: GraphStruct.() -> Unit): GraphStruct {
    val res = GraphStruct()
    res.lambda()
    return res
}
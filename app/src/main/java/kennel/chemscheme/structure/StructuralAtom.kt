package kennel.chemscheme.structure

open class StructuralAtom : BaseAtom {
    private val _conns: MutableSet<StructuralAtom>
    val conns: Set<StructuralAtom>
        get() = _conns

    val muchFreeSpace: Int
        get() {
            if(type == AtomType.Carbon) return (4 - conns.size)
            else return 0
        }

    val structuralParent: StructuralAtom?
    val isChild: Boolean

    constructor(
        id: Int,
        type: AtomType,
        isChild: Boolean = true,
        parent: StructuralAtom? = null,
        conns: Set<StructuralAtom> = setOf()
    ) : super(id, type) {
        _conns = conns.toMutableSet()
        this.isChild = isChild
        if ((isChild && parent == null) || (!isChild && parent != null)) {
            throw Exception("Bad parametrs of isChild - $isChild and parent - $parent for creating atom with id $id")
        } else {
            this.structuralParent = parent
            if (isChild) {
                _conns.add(parent!!)
            }
        }
    }


    fun addChild(atom: StructuralAtom) {
        if (muchFreeSpace > 0) {
            _conns.add(atom)
        } else {
            throw Exception("Not enough free space in atom with id $id to add atom with id ${atom.id}")
        }
    }

    open fun removeChild(childId: Int) {
        _conns.forEach {
            if (it.id == childId) {
                _conns.remove(it)
                return
            }
        }
        throw Exception("Atom with id $childId is not a child of atom with id $id")
    }
}
package kennel.chemscheme.structure

data class Atom2DLink(val atom: Sides2DAtom, val connType: Int)

class Sides2DAtom : StructuralAtom {
    private val _links: MutableList<Atom2DLink>
    val links: List<Atom2DLink>
        get() = _links

    val availableSides: Set<Int>
        get() = countAvailableSides()

    constructor(
        id: Int,
        type: AtomType,
        isChild: Boolean = true,
        parent: Sides2DAtom? = null,
        links: List<Atom2DLink> = listOf()
    ) : super(id, type, isChild, parent, links.map { it.atom }.toSet()) {

        _links = links.toMutableList()
        if (isChild) {
            _links.add(Atom2DLink(parent!!, -1))
        }
    }

    fun countAvailableSides(): Set<Int> {
        return if (type == AtomType.Carbon) {
            when (links.size) {
                3 -> setOf(3, 4, 5)
                in 0..2 -> {
                    val res = mutableSetOf<Int>()
                    setOf(0, 1, 2).forEach { av ->
                        var good = true
                        links.forEach { link -> if (av == link.connType) good = false }
                        if (good) res.add(av)
                    }
                    res
                }
                else -> emptySet()
            }
        } else {
            emptySet()
        }
    }

    fun addChild(child: Sides2DAtom, side: Int) {
        super.addChild(child)
        if (side in availableSides) {
            _links.add(Atom2DLink(child, side))
        } else {
            throw Exception("Side $side of atom with id $id is already occupied")
        }
    }

    override fun removeChild(childId: Int) {
        super.removeChild(childId)
        val link = findLinkById(childId)
        if (link != null) {
            _links.remove(findLinkById(childId))
        } else {
            throw Exception("No atom with id $childId is connected to atom with id ${this.id}")
        }
    }

    fun findLinkById(id: Int): Atom2DLink? {
        _links.forEach {
            if (it.atom.id == id) return it
        }
        return null
    }

    fun BaseAtom.toSidesAtom() = Sides2DAtom(id, type, links = links)
}
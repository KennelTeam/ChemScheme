package kennel.chemscheme.structure

class Atom2DLink(val atom: Sights2DAtom, val connType: Int)

class Sights2DAtom : StructuralAtom {
    private val _links: MutableList<Atom2DLink>
    val links: List<Atom2DLink>
        get() = _links

    val availableSights: Set<Int>
        get() = countAvailableSights()

    constructor(
        id: Int,
        type: AtomType,
        isChild: Boolean = true,
        parent: Sights2DAtom? = null,
        links: List<Atom2DLink> = listOf()
    )
            : super(id, type, isChild, parent, links.map { it.atom }.toSet()) {

        _links = links.toMutableList()
        if (isChild) {
            _links.add(Atom2DLink(parent!!, -1))
        }
    }

    fun countAvailableSights(): Set<Int> {
        when (links.size) {
            3 -> return setOf(3, 4, 5)
            in 0..2 -> {
                val res = mutableSetOf<Int>()
                setOf(0, 1, 2).forEach { av ->
                    var good = true
                    links.forEach { link -> if (av == link.connType) good = false }
                    if (good) res.add(av)
                }
                return res
            }
            else -> return emptySet()
        }
    }

    fun addChild(child: Sights2DAtom, sight: Int) {
        super.addChild(child)
        if (sight in availableSights) {
            _links.add(Atom2DLink(child, sight))
        } else {
            throw Exception("Sight $sight of atom with id $id is already occupied")
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

    protected fun findLinkById(id: Int): Atom2DLink? {
        _links.forEach {
            if (it.atom.id == id) return it
        }
        return null
    }

    fun BaseAtom.toSightsAtom() = Sights2DAtom(id, type, links = links)
}
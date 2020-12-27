package kennel.chemscheme.structure

class Atom2DLink(val atom: Sights2DAtom, val connType: Int)

open class BaseAtom {
    var type: AtomType
    val id: Int
    constructor(id: Int, type: AtomType) {
        this.id = id
        this.type = type
    }

    constructor() {
        id = Int.MIN_VALUE
        this.type = AtomType.None
    }
}

class Sights2DAtom: BaseAtom {
    private val _links: MutableList<Atom2DLink>
    val links: List<Atom2DLink>
        get() = _links

    val availableSights: Set<Int>
        get() = countAvailableSights()

    val parent: Sights2DAtom?

    private val isChild: Boolean

    constructor(id: Int, type: AtomType, isChild: Boolean = true, parent: Sights2DAtom? = null, links: List<Atom2DLink> = listOf()) : super(id, type) {
        _links = links.toMutableList()
        this.isChild = isChild
        if ((isChild == true && parent == null) || (isChild == false && parent != null)) {
            throw Exception("Bad parametrs of isChild and parent for creating atom with id $id")
        } else {
            this.parent = parent
            if (isChild) {
                _links.add(Atom2DLink(parent!!, -1))
            }
        }
    }

    private fun countAvailableSights(): Set<Int> {
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
        if (sight in availableSights) {
            _links.add(Atom2DLink(child, sight))
        } else {
            throw Exception("Sight $sight of atom with id $id is already occupied")
        }
    }

    fun removeChild(id: Int) {
        val link = findLinkById(id)
        if (link != null) {
            _links.remove(findLinkById(id))
        } else {
            throw Exception("No atom with id $id is connected to atom with id ${this.id}")
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
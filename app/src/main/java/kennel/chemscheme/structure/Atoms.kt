package kennel.chemscheme.structure

class AtomLink(val atom: BaseAtom, val connType: ConnSight)

open class BaseAtom {
    private val _links: MutableList<AtomLink>
    val links: List<AtomLink>
        get() = _links

    val type: AtomType

    constructor(type: AtomType, linkedTo: BaseAtom, linkSight: ConnSight) {
        this.type = type
        _links = mutableListOf(AtomLink(linkedTo, linkSight))
    }

    constructor(type: AtomType) {
        this.type = type
        _links = mutableListOf()
    }

    constructor(type: AtomType, links: List<AtomLink>) {
        this.type = type
        this._links = links.toMutableList()
    }

    fun addNeighbour(neighbour: BaseAtom, sight: ConnSight) { _links.add(AtomLink(neighbour, sight)) }
    fun removeNighbour(neighbour: BaseAtom) { _links.remove(findLink(neighbour)) }

    protected fun findLink(atom: BaseAtom): AtomLink? {
        _links.forEach {
            if (it.atom == atom) return it
        }
        return null
    }
}

class SightsAtom: BaseAtom {
    constructor(type: AtomType, linkedTo: SightsAtom, linkSight: ConnSight) : super(type, linkedTo, linkSight)
    constructor(type: AtomType, links: List<AtomLink>) : super(type, links)

    private val curConnections = links
    val availableSights: List<ConnSight>
        get() = countAvailableSights()

    private fun countAvailableSights(): List<ConnSight> {
        val available: List<ConnSight>
        available = when {
            curConnections.size in 1..2 -> {
                val pa: MutableSet<ConnSight> =
                    if (curConnections[0].connType in ConnSightsGroups.DIAL) {
                        ConnSightsGroups.DIAL.toMutableSet()
                    } else {
                        ConnSightsGroups.DELTA_DIAL.toMutableSet()
                    }
                curConnections.forEach { pa.remove(it) }
                pa.toList()
            }
            curConnections.isEmpty() -> {
                ConnSightsGroups.DIAL.toList() + ConnSightsGroups.DELTA_DIAL.toList()
            }
            else -> {
                listOf()
            }
        }
        return available
    }

    fun BaseAtom.toSightsAtom(): SightsAtom = SightsAtom(type, links)
}
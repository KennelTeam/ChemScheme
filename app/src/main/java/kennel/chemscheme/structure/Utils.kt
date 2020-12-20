package kennel.chemscheme.structure

enum class AtomType {
    Carbon,
    Hydrogen,
    Oxygen,
    Chlorum,
    Bromium,
    Iodine,
    Fluorine
}

object ConnSightsGroups {
    val DIAL: Set<ConnSight> = setOf(ConnSight.North, ConnSight.SouthEast, ConnSight.SouthWest)
    val DELTA_DIAL: Set<ConnSight> = setOf(ConnSight.South, ConnSight.NorthEast, ConnSight.NorthWest)
}

enum class ConnSight {
    North,
    SouthWest,
    SouthEast,
    South,
    NorthWest,
    NorthEast
}
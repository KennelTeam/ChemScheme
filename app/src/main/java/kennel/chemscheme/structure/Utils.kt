package kennel.chemscheme.structure

enum class AtomType {
    C, H, O, Cl, Br, I, F
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
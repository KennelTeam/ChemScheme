package kennel.chemscheme.structure

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
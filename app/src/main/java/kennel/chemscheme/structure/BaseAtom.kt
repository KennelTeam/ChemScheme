package kennel.chemscheme.structure

import kotlinx.serialization.Serializable

@Serializable
open class BaseAtom(
    val id: Int,
    var name: AtomTypes,
    var linkedTo: BaseAtom
) {
    var links: MutableList<BaseAtom> = mutableListOf(linkedTo)
}

interface Sights {
    val availableSights: List<ConnTypes>
}

class SightsAtom(id: Int, name: AtomTypes): BaseAtom
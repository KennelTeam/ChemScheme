package kennel.chemscheme.structure

object IdGenerator {
    var cur: Int = Int.MIN_VALUE
    fun genNewId(): Int {
        return ++cur
    }
}
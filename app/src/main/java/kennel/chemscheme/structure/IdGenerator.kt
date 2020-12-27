package kennel.chemscheme.structure

object IdGenerator {
    var cur: Int = 0
    fun genNewId(): Int {
        return ++cur
    }
}
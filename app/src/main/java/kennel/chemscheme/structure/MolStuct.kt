package kennel.chemscheme.structure

class Atom(name: String, links: Array<Int>) {
    var links: Array<Int> = links;
    var name: String = name;
}

fun appendA(arr: Array<Atom>, element: Atom): Array<Atom> {
    val list: MutableList<Atom> = arr.toMutableList()
    list.add(element)
    return list.toTypedArray()
}

fun append(arr: Array<Int>, element: Int): Array<Int> {
    val list: MutableList<Int> = arr.toMutableList()
    list.add(element)
    return list.toTypedArray()
}

class Structure() {
    var vertses = emptyArray<Atom>();
    fun add(Name: String, Binding: Int, Sight: Int) { // добавляет атом к конструкции
        vertses += Atom(Name, arrayOf());
        // Функция ещё не дописана
    }
    fun tie(A: Int, B: Int) { //Связывает два атома между собой (скорее всего будет удалена)
        vertses[A].links += arrayOf(B);
    }
    fun getByName(Name: String) {
        // Возвращает список всех атомов с данным именем (хз зачем она нужна)
    }
}
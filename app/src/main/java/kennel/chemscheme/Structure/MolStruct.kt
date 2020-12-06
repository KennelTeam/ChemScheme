package kennel.chemscheme.structure
//import PyCringe

fun Array<Int>.PySlice(A: Int, B: Int): Array<Int> {
    var answ: Array<Int> = arrayOf();
    for (i: Int in A..B) {
        answ += arrayOf(this[i]);
    }
    return answ;
}

fun Array<Int>.PyRemove(index: Int): Array<Int> {
    return this.PySlice(0, index - 1) + this.PySlice(index + 1, this.size - 1);
}

class MolStruct {

    enum class Elements {
        C, H, O, Cl, Br, I, F
    }

    class Atom(var name: Elements, var links: Array<Int>) {
        override fun toString(): String {
            return name.toString() + ", links: " + links.toString()
        }
    }

    class Structure() {
        var vertses = emptyArray<Atom>();
        fun add(Name: Elements, Binding: Int, Sight: Int) { // добавляет атом к конструкции
            vertses += arrayOf(Atom(Name, arrayOf()));
            if(Binding >= 0) {
                vertses[Binding].links =
                    vertses[Binding].links.PySlice(0, Sight - 1) + arrayOf(vertses.size - 1) +
                            vertses[Binding].links.PySlice(
                                Sight - 1,
                                vertses[Binding].links.size - 1
                            )
            }
        }

        fun pop(index: Int) {
            for (ind: Int in vertses.indices) {
                for (j: Int in vertses[ind].links.indices) {
                    if (vertses[ind].links[j] == index) {
                        vertses[ind].links = vertses[ind].links.PyRemove(j);
                    }
                }
            }
        }

        fun getByName(Name: Elements): Array<Int> {
            var answ = emptyArray<Int>();
            for (i: Int in vertses.indices) {
                if (vertses[i].name == Name) {
                    answ += arrayOf(i);
                }
            }
            return answ;
        }
    }
}

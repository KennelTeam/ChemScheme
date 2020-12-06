package kennel.chemscheme.Structure

import android.content.Context
import android.util.Log
import java.io.*

//import PyCringe

fun writeToFile(data: String, context: Context) {
    try {
        val outputStreamWriter = OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE))
        outputStreamWriter.write(data)
        outputStreamWriter.close()
    } catch (e: IOException) {
        Log.e("Exception", "File write failed: " + e.toString())
    }
}

fun readFromFile(context: Context): String? {
    var ret = ""
    try {
        val inputStream: InputStream? = context.openFileInput("config.txt")
        if (inputStream != null) {
            val inputStreamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            var receiveString: String? = ""
            val stringBuilder = StringBuilder()
            while (bufferedReader.readLine().also({ receiveString = it }) != null) {
                stringBuilder.append("\n").append(receiveString)
            }
            inputStream.close()
            ret = stringBuilder.toString()
        }
    } catch (e: FileNotFoundException) {
        Log.e("login activity", "File not found: " + e.toString())
    } catch (e: IOException) {
        Log.e("login activity", "Can not read file: $e")
    }
    return ret
}

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
        // ЗДЕСЬ МОГ БЫТЬ ВАШ КОД
    }

    class Atom(var name: Elements, var links: Array<Int>) {}

    class Structure() {
        var vertses = emptyArray<Atom>();
        fun add(Name: Elements, Binding: Int, Sight: Int) { // добавляет атом к конструкции
            vertses += arrayOf(Atom(Name, arrayOf(Binding)));
            if (Binding >= 0) {
                vertses[Binding].links =
                        vertses[Binding].links.PySlice(0, Sight - 1) + arrayOf(vertses.size - 1) +
                                vertses[Binding].links.PySlice(Sight, vertses[Binding].links.size - 1);
            }
        }

        fun pop(index: Int) {
            for (ind: Int in vertses.indices) {
                for (j: Int in vertses[ind].links.indices) {
                    if (vertses[ind].links[j] == index) {
                        vertses[ind].links = vertses[ind].links.PyRemove(j);
                    }
                    if (vertses[ind].links[j] > index) {
                        vertses[ind].links[j] -= 1;
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

        fun convert(): String {
            return ""
        }

        fun write(fname: String) {

        }
    }
}
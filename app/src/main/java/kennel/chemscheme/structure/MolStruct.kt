package kennel.chemscheme.structure

import kotlinx.serialization.Serializable


import android.content.Context
import android.util.Log
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import java.io.*

fun writeToFile(data: String, context: Context) {
    try {
        val outputStreamWriter = OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE))
        outputStreamWriter.write(data)
        outputStreamWriter.close()
    } catch (e: IOException) {
        Log.e("Exception", "File write failed: " + e.toString())
    }
}

fun readFromFile(context: Context): String {
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

class MolStruct {
    @Serializable
    class Structure {
        var vertses = emptyArray<BaseAtom>();
        fun add(type: AtomTypes, connection: BaseAtom, sight: Int) {
            if(connection >= 0) {
                vertses += arrayOf(BaseAtom(Name, arrayOf(connection)));
                vertses[connection].links =
                        vertses[connection].links.PySlice(0, sight - 1) + arrayOf(vertses.size - 1) +
                                vertses[connection].links.PySlice(sight, vertses[connection].links.size - 1);
            } else {
                vertses += arrayOf(BaseAtom(Name, arrayOf()));
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

        fun getByName(Name: AtomTypes): Array<Int> {
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
// Lesha PES

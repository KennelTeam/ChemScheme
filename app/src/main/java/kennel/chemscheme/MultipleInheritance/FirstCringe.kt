package kennel.chemscheme.MultipleInheritance

import android.util.Log

interface FirstCringe {
    fun doCringe()
}

internal class FCringe : FirstCringe{
    override fun doCringe() {
        Log.i("test", "this is first cringe")
    }
}

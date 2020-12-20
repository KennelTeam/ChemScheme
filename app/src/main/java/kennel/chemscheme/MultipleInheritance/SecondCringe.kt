package kennel.chemscheme.MultipleInheritance

import android.util.Log

interface SecondCringe {
    val T : Int

    fun doOtherCringe()
}

internal class SCringe : SecondCringe {
    override fun doOtherCringe() {
        Log.i("test", "this is other cringe")
    }
}

package kennel.chemscheme.positionProcessing

import android.util.Log

class Structure3D {
    var vertices = mutableListOf<Atom3D>()

    fun show(){
        for (vertex in vertices){
            Log.i("Structure3D", vertex.toString())
        }
    }
}
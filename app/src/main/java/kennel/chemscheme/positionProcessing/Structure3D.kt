package kennel.chemscheme.positionProcessing

import android.util.Log

class Structure3D(var vertices: MutableList<Atom3D> = mutableListOf()) {

    fun show(){
        for (vertex in vertices){
            Log.i("Structure3D", vertex.toString())
        }
    }
}
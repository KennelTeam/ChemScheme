package kennel.chemscheme.positionProcessing

import android.util.Log

//То же, что и Structure, но вместо Atom использует Atom3D
class Structure3D {
    var vertices = mutableListOf<Atom3D>()

    fun show(){
        for (vertex in vertices){
            Log.i("Structure3D", vertex.toString())
        }
    }
}
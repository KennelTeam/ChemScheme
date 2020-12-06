package kennel.chemscheme.positionProcessing

import kennel.chemscheme.structure.Atom

class Atom3D (var atom: Atom){
    var position : Vector = Vector(0.0, 0.0, 0.0)
    var nearIndex : Int = -1
    var farIndex : Int = -1

    override fun toString() : String{
        return "position: " + position.toString() + "\tatom: " + atom.toString() + "\t" + nearIndex + "\t" + farIndex
    }
}
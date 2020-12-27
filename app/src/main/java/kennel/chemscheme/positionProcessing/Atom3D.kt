package kennel.chemscheme.positionProcessing

import kennel.chemscheme.structure.Atom2DLink
import kennel.chemscheme.structure.BaseAtom
import kennel.chemscheme.structure.StructuralAtom

/*
Класс, который хранит в себе обычный Atom, но еще и позицию
 */
class Atom3D(base : StructuralAtom) : StructuralAtom(base.id, base.type, base.isChild, base.structuralParent, base.conns) {
    //3d вектор позиции
    var position : Vector = Vector(0.0, 0.0, 0.0)
    //индексы связей (atom.links), которые "ближе" и "дальше" - чтобы отмечать на схеме с треугольниками
    var nearIndex : Int = 0
    var farIndex : Int = 0

    override fun toString() : String{
        return "position: $position\t$nearIndex\t$farIndex"
    }
}
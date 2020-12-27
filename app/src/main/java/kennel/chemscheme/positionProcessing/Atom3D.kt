package kennel.chemscheme.positionProcessing

import kennel.chemscheme.structure.Atom2DLink
import kennel.chemscheme.structure.BaseAtom
import kennel.chemscheme.structure.ConnSight

/*
Класс, который хранит в себе обычный Atom, но еще и позицию
 */
class Atom3D(base : BaseAtom = BaseAtom()) : BaseAtom(base.type, base.links) {
    //3d вектор позиции
    var position : Vector = Vector(0.0, 0.0, 0.0)
    //индексы связей (atom.links), которые "ближе" и "дальше" - чтобы отмечать на схеме с треугольниками
    var nearIndex : Atom2DLink = Atom2DLink(BaseAtom(), ConnSight.North)
    var farIndex : Atom2DLink = Atom2DLink(BaseAtom(), ConnSight.North)

    override fun toString() : String{
        return "position: $position\t$nearIndex\t$farIndex"
    }
}
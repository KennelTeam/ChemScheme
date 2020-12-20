package kennel.chemscheme.positionProcessing

import com.badlogic.gdx.utils.compression.lzma.Base
import kennel.chemscheme.structure.AtomLink
import kennel.chemscheme.structure.BaseAtom
import kennel.chemscheme.structure.ConnSight

/*
Класс, который хранит в себе обычный Atom, но еще и позицию
 */
class Atom3D(base : BaseAtom = BaseAtom()) : BaseAtom(base.type, base.links) {
    //3d вектор позиции
    var position : Vector = Vector(0.0, 0.0, 0.0)
    //индексы связей (atom.links), которые "ближе" и "дальше" - чтобы отмечать на схеме с треугольниками
    var nearIndex : AtomLink = AtomLink(BaseAtom(), ConnSight.North)
    var farIndex : AtomLink = AtomLink(BaseAtom(), ConnSight.North)

    override fun toString() : String{
        return "position: $position\t$nearIndex\t$farIndex"
    }
}
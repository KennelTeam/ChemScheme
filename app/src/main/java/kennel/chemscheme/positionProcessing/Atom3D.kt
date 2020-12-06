package kennel.chemscheme.positionProcessing

import kennel.chemscheme.structure.MolStruct.Atom

/*
Класс, который хранит в себе обычный Atom, но еще и позицию
 */
class Atom3D (var atom: Atom){
    //3d вектор позиции
    var position : Vector = Vector(0.0, 0.0, 0.0)
    //индексы связей (atom.links), которые "ближе" и "дальше" - чтобы отмечать на схеме с треугольниками
    var nearIndex : Int = -1
    var farIndex : Int = -1

    override fun toString() : String{
        return "position: $position\tatom: $atom\t$nearIndex\t$farIndex"
    }
}
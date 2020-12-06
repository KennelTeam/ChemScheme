package kennel.chemscheme.positionProcessing

import kennel.chemscheme.structure.*
import kotlin.math.sqrt

class PositionCalculator {
    companion object {
        fun findAllIsolated(graph : Structure) : MutableList<Atom>{
            var result = mutableListOf<Atom>()
            for (carbon : Atom in graph.getByName("C")){
                var carbonsFound = 0
                for (atom : Int in carbon.links){
                    if(graph.vertses[atom].name == "C"){
                        carbonsFound++
                    }
                }
                if(carbonsFound < 2){
                    result.add(carbon)
                }
            }

            return result
        }

        fun getLongestCarbon(graph : Structure, vertex : Atom, alreadyVisited : MutableList<Atom>) : MutableList<Atom>{
            var result = mutableListOf<Atom>()
            alreadyVisited.add(vertex)
            result.add(vertex)
            for (index : Int in vertex.links){
                if(!alreadyVisited.contains(graph.vertses[index])){
                    var variant = getLongestCarbon(graph, graph.vertses[index], alreadyVisited)
                    if(variant.size >= result.size){
                        result = mutableListOf<Atom>(vertex)
                        result.addAll(variant)
                    }
                }
            }
            return result
        }

        fun rotateVector(right : Vector, left: Vector, near: Boolean) : Vector{
            val halfSum = -(right + left) / 2.0
            val diff = (left - right) / 2.0
            var cross = Vector.constants.zero
            if(near){
                cross = diff * halfSum
            } else {
                cross = halfSum * diff
            }

            return halfSum + (cross.unit() * diff.magnitude())
        }

        fun drawSequence(graph: Structure, sequence : MutableList<Atom3D>, right : Vector, left : Vector) : MutableList<Atom3D>{
            var result = mutableListOf<Atom3D>()
            var current = sequence[0].position
            for (index : Int in 1..sequence.size){
                if(index % 2 == 0){
                    current += right
                } else {
                    current += left
                }
                sequence[index].position = current

                var alreadyFound : Boolean = false

                var neighbours = mutableListOf<Atom>()
                neighbours.add(sequence[index].atom)
                neighbours.add(sequence[index - 1].atom)
                if(index != sequence.size - 1){
                    neighbours.add(sequence[index + 1].atom)
                }

                for (atomInd : Int in sequence[index].atom.links){
                    var inSequence : Boolean = false
                    for (atom : Atom3D in sequence){
                        if(atom.atom == graph.vertses[atomInd]){
                            inSequence = true
                            break
                        }
                    }
                    if(inSequence){
                        continue
                    }

                    if (alreadyFound){
                        sequence[index].farIndex = atomInd
                    } else{
                        sequence[index].nearIndex = atomInd
                        alreadyFound = true
                    }
                    var nextDirection : Vector = rotateVector(right, left, alreadyFound)

                    var nextSequence = mutableListOf<Atom>(sequence[index].atom)
                    nextSequence.addAll(getLongestCarbon(graph, graph.vertses[atomInd], neighbours))

                    var nextSequence3d = mutableListOf<Atom3D>()
                    for (atom : Atom in nextSequence){
                        nextSequence3d.add(Atom3D(atom))
                    }

                    result.addAll(drawSequence(graph, nextSequence3d, right, nextDirection))
                }

                result.add(sequence[index])
            }

            return result
        }

        fun <T> mutableListWithCapacity(capacity: Int): MutableList<T> =
            ArrayList(capacity)

        fun calculatePositions(structure : Structure) : Structure3D {
            val isolated = findAllIsolated(structure)
            var theLongestCarbonSequence = mutableListOf<Atom>()
            for (v : Atom in isolated){
                for (index : Int in v.links){
                    if(structure.vertses[index].name != "C"){
                        val longSequence = getLongestCarbon(structure, structure.vertses[index], mutableListOf<Atom>())
                        if(longSequence.size > theLongestCarbonSequence.size){
                            theLongestCarbonSequence = longSequence
                        }
                    }
                }
            }

            var sequence3d = mutableListOf<Atom3D>()
            for (atom : Atom in theLongestCarbonSequence){
                sequence3d.add(Atom3D(atom))
            }

            var atoms3d = drawSequence(structure, sequence3d, Vector(sqrt(3.0), 1.0, 0.0), Vector(
                sqrt(3.0), -1.0, 0.0)
            )

            var result : Structure3D = Structure3D()
            result.vertices = mutableListWithCapacity<Atom3D>(atoms3d.size)
            for (atom : Atom3D in atoms3d){
                result.vertices.add(structure.vertses.indexOf(atom.atom), atom)
            }

            return result
        }
    }

}
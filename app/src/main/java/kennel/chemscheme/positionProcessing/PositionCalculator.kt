package kennel.chemscheme.positionProcessing

import android.util.Log
import com.badlogic.gdx.utils.compression.lzma.Base
import kennel.chemscheme.structure.AtomLink
import kennel.chemscheme.structure.BaseAtom
import kennel.chemscheme.structure.MolStruct
import kennel.chemscheme.structure.AtomType
import kennel.chemscheme.structure.MolStruct.*
import kotlin.math.min
import kotlin.math.sqrt

//Основной (по сути статический) класс для вычисления 3d позиций атомов в молекуле
class PositionCalculator {
    companion object {
        //Ищет все одноричные углероды. Используется при поиске самой длинной углеродной цепочки
        private fun findAllIsolated(graph : MolStruct) : MutableList<BaseAtom>{
            var result = mutableListOf<BaseAtom>()
            //Идем по всем углеродам
            for (carbon : BaseAtom in graph.getByName(AtomType.Carbon)){
                //Считаем, сколько у него соседей-углеродов
                var carbonsFound = 0
                for (link : AtomLink in carbon.links){
                    if(link.atom.type == AtomType.Carbon){
                        carbonsFound++
                    }
                }
                //Если мало, то добавляем в итоговый список
                if(carbonsFound < 2){
                    result.add(carbon)
                }
            }

            return result
        }

        //Находит самую длинную цепочку углеродов, которая начинается с атома vertex.
        //Рекурсивная функция
        //graph - молекула, в которой ищем
        //vertex - атом, с которого должна начинаться цепочка
        //alreadyVisited - множество вершин, в которых мы уже побывали (их не надо проверять)
        private fun getLongestCarbon(graph : MolStruct, vertex : BaseAtom, alreadyVisited : MutableSet<BaseAtom>) : MutableList<BaseAtom>{
            var result = mutableListOf<BaseAtom>()
            alreadyVisited.add(vertex)
            result.add(vertex)
            //Идем по всем соседям этого атома
            for (link : AtomLink in vertex.links){
                //которые при этом мы еще не рассмотрели
                if(!alreadyVisited.contains(link.atom)){
                    //аналогично выполняем функцию для этого соседа
                    var variant = getLongestCarbon(graph, link.atom, alreadyVisited)
                    //Если полученная цепочка больше, чем то, что есть сейчас,
                    //то заменяем старую на полученную только что
                    if(variant.size + 1 > result.size){
                        result = mutableListOf<BaseAtom>(vertex)
                        result.addAll(variant)
                    }
                }
            }
            return result
        }

        /*Функция, находящая другую плоскость
        //Если основная цепочка идет в плоскости, заданной векторами left и right
        то цепочки, отходящие от нее будут в двух других плоскостях
        обе плоскости задаются векторами right и новым вектором,
        который как раз получается из этой функции
        Другой способ объяснить:
        Углерод - это центр треугольной правильно пирамиды. Два данных вектора - это позиции
        двух вершин относительно центра. Тогда полученные два вектора - это две другие вершины

        near - это переменая, которая переключается между двумя вариантами (двумя вершинами),
        которые можно получить
        */
        private fun rotateVector(right : Vector, left: Vector, near: Boolean) : Vector{
            //Тут реализуется просто математическая формула.. немного стереометрии и все готово
            val r = -right
            val halfSum = -(r + left) / 2.0
            val diff = (left - r) / 2.0
            var cross = Vector.constants.zero
            if(near){
                //Используется векторное умножение! (cross product)
                cross = diff * halfSum
            } else {
                cross = halfSum * diff
            }

            return halfSum + (cross.unit() * diff.magnitude())
        }

        /* Основная функция. Для данной цепочки углеродов находит их позиции и так же рекурсивно
        запускается для всех цепочек, которые выходят из этой

        graph - молекула, в которой все происходит
        sequence - цепочка атомов (которая основная в данном случае)
        right, left - вектора, которые задают плоскость, в которой располагается вся эта цепочка.
        При том эти вектора - это направления атомов (типа они как раз задают зигзаг,
        который получается из этой формулы)
         */
        private fun drawSequence(graph: MolStruct, sequence : MutableList<Atom3D>, right : Vector, left : Vector) : MutableList<Atom3D>{
            //Итоговый массив с Atom3D
            var result = mutableListOf<Atom3D>()
            //Позиция i-го атома (считаеся, что 0-й атом уже поставлен на нужное место и остальные
            //атомы выстраиваются относительно него)
            var current = sequence[0].position
            //Идем по всем остальным атомам
            for (index : Int in 1..sequence.size-1){
                //Зигзаг идет либо вниз либо вверх
                if(index % 2 == 0){
                    current += right
                } else {
                    current += left
                }
                //И назначаем полученную позицию нашему атому
                sequence[index].position = current

                //Теперь ищем побочные цепочки
                //Если мы уже встретили одну (она ушла, скажем, вправо), то следующая должна уйти влево
                var alreadyFound : Boolean = false

                //Чтобы при поиске цепочек не найти кусок той цепочки, по которой мы и так сейчас идем
                //скажем, что во всех атомах нашей основной цепочки мы "уже были"
                var neighbours = mutableSetOf<BaseAtom>()
                neighbours.add(sequence[index])
                neighbours.add(sequence[index - 1])
                if(index != sequence.size - 1){
                    neighbours.add(sequence[index + 1])
                }

                //Теперь идем по всем остальным атомам, которые соединены с i-м атомом цепочки,
                //но не включены в цепочку
                for (atomLink : AtomLink in sequence[index].links){
                    //Отбросим все, которые включены в цепочку
                    var inSequence : Boolean = false
                    for (atom : Atom3D in sequence){
                        if(atom == atomLink.atom){
                            inSequence = true
                            break
                        }
                    }
                    if(inSequence){
                        continue
                    }
                    //Измененная плоскость будет задачаться другими векторами (найдем их)
                    var nextDirection : Vector = rotateVector(right, left, alreadyFound)
                    //В зависимости от того, первая это или вторая побочная цепочка, назовем ее ближней или дальней
                    if (alreadyFound){
                        sequence[index].farIndex = atomInd
                    } else{
                        sequence[index].nearIndex = atomInd
                        alreadyFound = true
                    }

                    //Теперь подготовим все для рекурсивного запуска: создадим цепочку, по которой надо пройти
                    var nextSequence = mutableListOf<Atom>(sequence[index].atom)
                    //Найдем самую длинную цепочку, отходящую от этого атома
                    nextSequence.addAll(getLongestCarbon(graph, graph.vertses[atomInd], neighbours))
                    //Построим ее в виде 3d цепочки (вместо Atom поставим Atom3D)
                    var nextSequence3d = mutableListOf<Atom3D>()
                    nextSequence3d.add(sequence[index])
                    for (i : Int in 1 until nextSequence.size){
                        nextSequence3d.add(Atom3D(nextSequence[i]))
                    }

                    var secondDirection = if(index % 2 == 0) right else left
                    nextDirection = if(index % 2 == 0) nextDirection else -nextDirection

                    //И вызовем рекурсивную функцию
                    result.addAll(drawSequence(graph, nextSequence3d, secondDirection, nextDirection))
                }
                //Ну теперь можно и добавить наш атом в список готовых (обработанных) атомов
                result.add(sequence[index])
            }

            return result
        }

        // Функция, которую надо вызывать. Рассчитывает для данной молекулы ее 3d позиции
        fun calculatePositions(structure : Structure) : Structure3D {
            //Для начала найдем все углероды, которые ближе всего к "краю" молекулы
            val isolated = findAllIsolated(structure)
            //Будем искать самую длинную цепочку атомов во всей молекуле
            var theLongestCarbonSequence = mutableListOf<Atom>()
            for (v : Atom in isolated){
                //Такая цепочка точно должна начинаться с атома, который соединен с одноричным углеродом
                for (index : Int in v.links){
                    if(structure.vertses[index].name != Elements.C){
                        //Для каждого такого атома запустим алгоритм поиска самой длинной цепочки
                        val longSequence = getLongestCarbon(structure, structure.vertses[index], mutableSetOf())
                        //И если она длиннее, чем максимальная найденная, то заменим максимальную
                        if(longSequence.size > theLongestCarbonSequence.size){
                            theLongestCarbonSequence = longSequence
                        }
                    }
                }
            }

            //Создадим 3d последовательность (с Atom3D)
            var sequence3d = mutableListOf<Atom3D>()
            for (atom : Atom in theLongestCarbonSequence){
                sequence3d.add(Atom3D(atom))
            }
            var atoms3d = mutableListOf<Atom3D>(sequence3d[0])
            //Вызовем основную функцию и найдем позиции всех атомов
            atoms3d.addAll(drawSequence(structure, sequence3d, Vector(sqrt(3.0), 1.0, 0.0), Vector(
                sqrt(3.0), -1.0, 0.0)
            ))

            var minDistance = Float.MAX_VALUE
            for (atom1 in atoms3d){
                for (atom2 in atoms3d){
                    if(atom1 != atom2) {
                        var distance = (atom1.position - atom2.position).magnitude().toFloat()
                        minDistance = min(minDistance, distance)
                    }
                }
            }

            if(minDistance > 0){
                var coefficient = 1f / minDistance

                for (atom in atoms3d){
                    atom.position = atom.position * coefficient.toDouble()
                    Log.i("test", "atom " + "${atom.position}")
                }
            }

            //Сохраним результат в виде 3d структуры
            var result : Structure3D = Structure3D()
            result.vertices = mutableListOf<Atom3D>()
            for (atom in atoms3d){
                result.vertices.add(atom)
            }
            for (atom : Atom3D in atoms3d){
                result.vertices[structure.vertses.indexOf(atom.atom)] = atom
            }

            return result
        }
    }

}
package kennel.chemscheme.positionProcessing

import kotlin.math.sqrt

class Vector (var x : Double, var y : Double, var z : Double) {

    object constants{
        val zero = Vector(0.0, 0.0, 0.0)
    }

    operator fun minus(other : Vector) : Vector {
        return Vector(x - other.x, y - other.y, z - other.z)
    }

    operator fun plus(other : Vector) : Vector {
        return Vector(x + other.x, y + other.y, z + other.z)
    }

    operator fun unaryMinus() : Vector {
        return Vector(-x, -y, -z)
    }

    fun magnitude() : Double {
        return sqrt(x*x + y*y + z*z)
    }

    operator fun div(k : Double) : Vector {
        return Vector(x / k, y / k, z / k)
    }

    operator fun times(k : Double) : Vector {
        return Vector(x * k, y * k, z * k)
    }

    fun unit() : Vector {
        return (this / this.magnitude());
    }

    operator fun times(b : Vector) : Vector{
        return Vector(y*b.z - z*b.y, z*b.x - x*b.z, x*b.y - y*b.z)
    }

    override fun toString() : String{
        return "(" + x.toString() + ", " + y.toString() + ", " + z.toString() + ")"
    }
}
package linearAlgebra

import kotlin.math.sqrt

class Vector3(val x: Double, val y: Double, val z: Double) {

    val zero   get() = Vector3(0.0, 0.0, 0.0)
    val unitX  get() = Vector3(1.0, 0.0, 0.0)
    val unitY  get() = Vector3(0.0, 1.0, 0.0)
    val unitZ  get() = Vector3(0.0, 0.0, 1.0)
    val length get() = sqrt(x * x + y * y + z * z)

    constructor(): this(0.0, 0.0, 0.0)
    constructor(v: Vector3): this(v.x, v.y, v.z)
    constructor(x: Int, y: Int, z: Int) : this(x.toDouble(), y.toDouble(), z.toDouble())
    constructor(x: Float, y: Float, z: Float): this(x.toDouble(), y.toDouble(), z.toDouble())

    operator fun get(index: Int): Double {
        return when (index) {
            0 -> x
            1 -> y
            2 -> z
            else -> throw IndexOutOfBoundsException("Index out of bounds at $index")
        }
    }
    override fun toString()        = "[x = ${x}, y = ${y}, z = ${z}]"
    operator fun unaryPlus()       = Vector3(+x, +y, +z)
    operator fun unaryMinus()      = Vector3(-x, -y, -z)
    operator fun times(d: Double)  = Vector3(x * d, y * d, z * d)
    operator fun times(d: Float)   = Vector3(x * d, y * d, z * d)
    operator fun times(d: Int)     = Vector3(x * d, y * d, z * d)
    operator fun div  (d: Double)  = Vector3(x / d, y / d, z / d)
    operator fun div  (d: Float)   = Vector3(x / d, y / d, z / d)
    operator fun div  (d: Int)     = Vector3(x / d, y / d, z / d)
    operator fun plus (v: Vector3) = Vector3(x + v.x, y + v.y, z + v.z)
    operator fun minus(v: Vector3) = Vector3(x - v.x, y - v.y, z - v.z)
    fun dot(v: Vector3)  = x * v.x + y * v.y + z * v.z
    fun cross(v: Vector3) = Vector3(y * v.z - z * v.y, z * v.x - x * v.z, x * v.y - y * v.x)
    fun normalize(): Vector3 {
        return if(length == 0.0) Vector3().zero
        else {
            val scale = 1.0 / length
            Vector3 (x * scale, y * scale, z * scale)
        }
    }
}
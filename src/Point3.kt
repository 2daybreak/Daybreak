import linearAlgebra.Vector3

class Point3(val width : Double, val height: Double, val v: Vector3) {

    constructor(w: Int, h: Int, v: Vector3): this(w.toDouble(), h.toDouble(), v)
    constructor(size: Int, u: Vector3): this(size, size, u)

    operator fun get(index: Int): Double {
        return when (index) {
            0 -> v[0]
            1 -> v[1]
            2 -> v[2]
            else -> throw IndexOutOfBoundsException("Index out of bounds at $index")
        }
    }
    override fun toString() = "width = $width, height = $height," + v.toString()

    fun contains(u: Vector3): Boolean {
        val normx = (u[0] - v[0]) / width
        val normy = (u[1] - v[1]) / height
        val normz = (u[2] - v[2]) / height
        return normx * normx + normy * normy + normz * normz < 0.125
    }
    fun contains(x: Int, y: Int): Boolean {
        val normx = (x.toDouble() - v[0]) / width
        val normy = (y.toDouble() - v[1]) / height
        return normx * normx + normy * normy < 0.25
    }
    fun contains(x: Double, y: Double): Boolean {
        val normx = (x - v[0]) / width
        val normy = (y - v[1]) / height
        return normx * normx + normy * normy < 0.25
    }
}
package linearAlgebra

class Matrix3(val m: Array<DoubleArray>) {

        /*val m00: Double, val m01: Double, val m02: Double,
              val m10: Double, val m11: Double, val m12: Double,
              val m20: Double, val m21: Double, val m22: Double) { */

    operator fun get(rowIndex: Int, columnIndex: Int): Double {
        return m[rowIndex][columnIndex]
    }

    constructor():
            this(Array(3) { DoubleArray(3) })

    constructor(u: Vector3, v: Vector3, w: Vector3):
            this(arrayOf(doubleArrayOf(u.x, u.y, u.z),
                         doubleArrayOf(v.x, v.y, v.z),
                         doubleArrayOf(w.x, w.y, w.z)))

    val zero get()= Matrix3(Vector3().zero, Vector3().zero, Vector3().zero)

    val identity get() = Matrix3(Vector3().unitX, Vector3().unitY, Vector3().unitZ)

    //val inverse get() =

    val trace = m[0][0] + m[1][1] + m[2][2]

    val determinant =
            m[0][0] * m[1][1] * m[2][2] - m[0][0] * m[1][2] * m[2][1] +
            m[0][1] * m[1][2] * m[2][0] - m[0][1] * m[1][0] * m[2][2] +
            m[0][2] * m[1][0] * m[2][1] - m[0][2] * m[1][1] * m[2][0]

    val diagonal = Vector3(m[0][0], m[1][1], m[2][2])

    val row0 get() = Vector3(m[0][0], m[0][1], m[0][2])
    val row1 get() = Vector3(m[1][0], m[1][1], m[1][2])
    val row2 get() = Vector3(m[2][0], m[2][1], m[2][2])

    val column0 get() = Vector3(row0.x, row1.x, row2.x)
    val column1 get() = Vector3(row0.y, row1.y, row2.y)
    val column2 get() = Vector3(row0.z, row1.z, row2.z)

    fun mult(l: Matrix3) = Matrix3(
            arrayOf(
                    doubleArrayOf(
                            m[0][0] * m[0][0] + m[0][1] * l.m[1][0] + m[0][2] * m[2][0],
                            m[0][0] * m[0][1] + m[0][1] * l.m[1][1] + m[0][2] * m[2][1],
                            m[0][0] * m[0][2] + m[0][1] * l.m[1][2] + m[0][2] * m[2][2]),
                    doubleArrayOf(
                            m[1][0] * m[0][0] + m[1][1] * l.m[1][0] + m[1][2] * m[2][0],
                            m[1][0] * m[0][1] + m[1][1] * l.m[1][1] + m[1][2] * m[2][1],
                            m[1][0] * m[0][2] + m[1][1] * l.m[1][2] + m[1][2] * m[2][2]),
                    doubleArrayOf(
                            m[2][0] * m[0][0] + m[2][1] * l.m[1][0] + m[2][2] * m[2][0],
                            m[2][0] * m[0][1] + m[2][1] * l.m[1][1] + m[2][2] * m[2][1],
                            m[2][0] * m[0][2] + m[2][1] * l.m[1][2] + m[2][2] * m[2][2])
            ))

    operator fun times(l: Matrix3) = mult(l)
    operator fun times(d: Double) =
            Matrix3(row0 * d, row1 * d, row2 * d)
    operator fun div(d: Double) =
            Matrix3(row0 / d, row1 / d, row2 / d)

}
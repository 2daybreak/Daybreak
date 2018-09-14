package linearAlgebra

import kotlin.math.cos
import kotlin.math.sin

class Matrix3(val m: Array<DoubleArray>) {

    operator fun get(rowIndex: Int, columnIndex: Int): Double {
        return m[rowIndex][columnIndex]
    }

    constructor():
    this(arrayOf(doubleArrayOf(0.0, 0.0, 0.0),
                 doubleArrayOf(0.0, 0.0, 0.0),
                 doubleArrayOf(0.0, 0.0, 0.0)))

    constructor(u: Vector3, v: Vector3, w: Vector3):
            this(arrayOf(doubleArrayOf(u.x, u.y, u.z),
                         doubleArrayOf(v.x, v.y, v.z),
                         doubleArrayOf(w.x, w.y, w.z)))

    /**
     * 3-dimensional rotation matrix from axis and angle
     *
     * @param u a vector unchanged by the rotation
     * @param theta counterclockwise angle of rotation
     * @return the point on a curve
     */
    constructor(axis: Vector3, theta: Double) : this() {
        val u = axis.normalize()
        m[0][0] = cos(theta) + u.x * u.x * (1.0 - cos(theta))
        m[0][1] = u.x * u.y * (1.0 - cos(theta)) - u.z * sin(theta)
        m[0][2] = u.x * u.z * (1.0 - cos(theta)) - u.y * sin(theta)
        m[1][0] = m[0][1]
        m[1][1] = cos(theta) + u.y * u.y * (1.0 - cos(theta))
        m[1][2] = u.y * u.z * (1.0 - cos(theta)) - u.x * sin(theta)
        m[2][0] = m[0][2]
        m[2][1] = m[1][2]
        m[2][2] = cos(theta) + u.z * u.z * (1.0 - cos(theta))
    }

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
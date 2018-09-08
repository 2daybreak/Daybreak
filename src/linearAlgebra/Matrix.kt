package linearAlgebra

class Matrix(val m: Array<DoubleArray>) {

    operator fun get(rowIndex: Int, columnIndex: Int): Double {
        return m[rowIndex][columnIndex]
    }

    constructor(n: Int):
            this(n, n)

    constructor(irow: Int, icolumn: Int):
            this(Array(irow) { DoubleArray(icolumn) })

    val determinant get() = det(m)
    val identity    get() = Matrix(iso())
    val inverse     get() = Matrix(invertMatrix(m))

    private fun iso(): Array<DoubleArray> {
        if (m.size != m[0].size) throw Exception("Non-square matrix")
        val a = Array(m.size) { DoubleArray(m.size) }
        for (i in 0 until m.size) a[i][i] = 1.0
        return a
    }

    private fun det(m: Array<DoubleArray>): Double {
        val irow = m.size
        val icolumn = m[0].size
        if (irow != icolumn) throw Exception("Non-square matrix")
        when (m.size) {
            1 -> return m[0][0]
            2 -> return m[0][0] * m[1][1] - m[0][1] * m[1][0]
            else -> {
                val nm1 = m.size - 1
                var result = 0.0
                for (i in 0..nm1) {
                    var minor = Array(nm1) { DoubleArray(nm1) }
                    for (j in 1..nm1) {
                        for (k in 0..nm1) {
                            if (k < i) minor[j - 1][k] = m[j][k]
                            if (k > i) minor[j - 1][k - 1] = m[j][k]
                        }
                    }
                    result += m[0][i] * Math.pow(-1.0, i.toDouble()) *
                            det(minor)
                }
                return result
            }
        }
    }

    private fun invertMatrix(m: Array<DoubleArray>): Array<DoubleArray> {

        if (determinant == 0.0) throw Exception("Square matrix singular(not invertible)")

        val index = IntArray(m.size)
        val auxiliaryMatrix = Array(m.size) { DoubleArray(m.size) }
        val invertedMatrix  = Array(m.size) { DoubleArray(m.size) }

        for (i in m.indices) {
            auxiliaryMatrix[i][i] = 1.0
        }

        transformToUpperTriangle(m, index)

        for (i in 0 until m.size - 1) {
            for (j in i + 1 until m.size) {
                for (k in m.indices) {
                    auxiliaryMatrix[index[j]][k] -= m[index[j]][i] * auxiliaryMatrix[index[i]][k]
                }
            }
        }

        for (i in m.indices) {
            invertedMatrix[m.size - 1][i] = auxiliaryMatrix[index[m.size - 1]][i] / m[index[m.size - 1]][m.size - 1]

            for (j in m.size - 2 downTo 0) {
                invertedMatrix[j][i] = auxiliaryMatrix[index[j]][i]

                for (k in j + 1 until m.size) {
                    invertedMatrix[j][i] -= m[index[j]][k] * invertedMatrix[k][i]
                }

                invertedMatrix[j][i] /= m[index[j]][j]
            }
        }

        return invertedMatrix
    }

    fun transformToUpperTriangle(m: Array<DoubleArray>, index: IntArray) {
        val c: DoubleArray
        var c0: Double
        var c1: Double
        var pi0: Double
        var pi1: Double
        var pj: Double
        var itmp: Int
        var k: Int

        c = DoubleArray(m.size)

        for (i in m.indices) {
            index[i] = i
        }

        for (i in m.indices) {
            c1 = 0.0

            for (j in m.indices) {
                c0 = Math.abs(m[i][j])

                if (c0 > c1) {
                    c1 = c0
                }
            }

            c[i] = c1
        }

        k = 0

        for (j in 0 until m.size - 1) {
            pi1 = 0.0

            for (i in j until m.size) {
                pi0 = Math.abs(m[index[i]][j])
                pi0 /= c[index[i]]

                if (pi0 > pi1) {
                    pi1 = pi0
                    k = i
                }
            }

            itmp = index[j]
            index[j] = index[k]
            index[k] = itmp

            for (i in j + 1 until m.size) {
                pj = m[index[i]][j] / m[index[j]][j]
                m[index[i]][j] = pj

                for (l in j + 1 until m.size) {
                    m[index[i]][l] -= pj * m[index[j]][l]
                }
            }
        }
    }


}


/*
    val trace = m00 + m11 + m22

    val diagonal = linearAlgebra.Vector3(m00, m11, m22)
*/
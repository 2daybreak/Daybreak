package geoModel

import linearAlgebra.Vector3
import linearAlgebra.MatrixSolvLU.*

class InterpolatedBspline: Bspline {

    private val pts = mutableListOf<Vector3>()
    private val slp = mutableListOf<Vector3>()
    private val isl = mutableListOf<Int>()
    private var chord = 0.0
    var alpha = 1.0

    constructor() : this(3)

    constructor(max: Int) : super(max)

    constructor(max: Int, p: MutableList<Vector3>) : super(max) {
        for(v in p) pts.add(v)
        properties(pts); evalCtrlPoints()
    }

    override fun properties(p: MutableList<Vector3>) {
        super.properties(p)
        var sum = 0.0
        for (i in 1 until p.size) {
            val del = p[i] - p[i - 1]
            sum += del.length
        }
        chord = sum * alpha
    }

    override fun addPts(v: Vector3) {
        pts.add(v)
        properties(pts); evalCtrlPoints()
    }

    override fun addPts(i: Int, v: Vector3) {
        pts.add(i, v)
        properties(pts); evalCtrlPoints()
    }

    override fun modPts(i: Int, v: Vector3) {
        pts[i] = v
        properties(pts); evalCtrlPoints()
    }

    override fun removePts(i: Int) {
        if (i != -1) pts.removeAt(i)
        if (!pts.isEmpty()) {
            properties(pts); evalCtrlPoints()
        }
    }

    override fun addSlope(i: Int, v: Vector3) {
        if(isl.contains(i)) modSlope(i, v)
        else {
            slp.add(v)
            isl.add(i)
        }
        properties(pts); evalCtrlPoints()
    }

    override fun modSlope(i: Int, v: Vector3) {
        if(isl.contains(i)) {
            val ind = isl.indexOf(i)
            slp[ind] = v
            isl[ind] = i
        }
        properties(pts); evalCtrlPoints()
    }

    override fun removeSlope(i: Int) {
        if(isl.contains(i)) {
            val ind = isl.indexOf(i)
            slp.removeAt(ind)
            isl.removeAt(ind)
        }
        properties(pts); evalCtrlPoints()
    }

    override fun evalKnots() {
        knots.clear()
        val n = pts.size
        val nm1 = n - 1
        for (i in 1..order) knots.add(0.0)
        for (i in 1..order) knots.add(1.0)
        for (i in 1..pts.size -order) {
            var sum = 0.0
            //averaging spacing(reflecting the distribution of prm)
            for (j in i until i + degree) sum += prm[j]
            sum /= degree
            val ipm = degree - 2 + i
            when(isl.contains(ipm)) {
                true -> {
                    knots.add(0.67 * sum + 0.33 * prm[ipm - 1])
                    knots.add(0.67 * sum + 0.33 * prm[ipm + 1])
                }
                false ->
                    knots.add(sum)
            }
        }
        for (i in slp.indices) {
            val index = isl[i]
            when (index) {
                0 -> {
                    val t = 0.5 * (prm[0] + prm[1])
                    knots.add(t)
                }
                nm1 -> {
                    val t = 0.5 * (prm[nm1] + prm[nm1 - 1])
                    knots.add(t)
                }
                in 1..degree - 2 -> knots.add(prm[index])
                in nm1 - 1..nm1 - degree + 2 -> knots.add(prm[index])
            }
        }
        knots.sort()
        println("prm= $prm")
        println("knots= $knots")
    }

    override fun uniformKnots(n: Int) {
        super.uniformKnots(n + slp.size)
        println(knots)
    }

    private fun evalCtrlPoints() {
        // Evaluate B-spline control points by the given points on a curve
        ctrlPts.clear()
        solveLU(pts, slp)
    }

    /**
     * Solve linear algebra of AX = B, Sum( Ni(Ui) * Pi ) = Qi
     * where i = 0..n,  n = count() - 1
     */
    //Algorithm 9.1
    private fun solveLU(p: MutableList<Vector3>) {
        val n = p.size
        val aa = Array(n) { DoubleArray(n) }
        for (i in 0 until n) {
            val span = findIndexSpan(n, prm[i])
            val nn = basisFuncs(span, prm[i])
            for (j in 0..degree) aa[i][span - degree + j] = nn[j]
        }
        if (n >= 3) {
            val indx = IntArray(n)
            ludcmp(n, aa, indx)
            ctrlPts.addAll(lubksb(n, aa, indx, p.toTypedArray()))
        }
        else ctrlPts.addAll(p)
    }

    /**
     * Solve linear algebra of AX = B, Sum( Ni(Uk) * Pi ) = Qk. Note that basis
     * funcs (& also knots), ctrlPts are in i = 0..pts.size + Slope.size, while
     * prm, pts are in k = 0..pts.size
     */
    private fun solveLU(p: MutableList<Vector3>, v: MutableList<Vector3>) {
        val n = p.size
        val nm1 = n - 1
        val m = n + v.size
        val aa = Array(m) { DoubleArray(m) }
        for (i in 0 until n) {
            val span = findIndexSpan(m, prm[i])
            val nn = basisFuncs(span, prm[i])
            for (j in 0..degree) aa[i][span - degree + j] = nn[j]
        }
        val bb = Array(m) {Vector3()}
        for (i in 0 until n) {
            bb[i] = p[i]
        }
        for (i in 0 until v.size) {
            val npi = n + i
            when (isl[i]) {
                0 -> {
                    aa[npi][0] = -1.0
                    aa[npi][1] = 1.0
                    bb[npi] = v[i] * knots[order] / degree * chord
                }
                nm1 -> {
                    aa[npi][m - 2] = -1.0
                    aa[npi][m - 1] = 1.0
                    bb[npi] = v[i] * (1 - knots[knots.size - 1 - order]) / degree * chord
                }
                else -> {
                    val span = findIndexSpan(m, prm[isl[i]])
                    val nn = dersBasisFunc(1, span, prm[isl[i]])
                    for (j in 0..degree)
                        aa[npi][span - degree + j] = nn[1][j]
                    bb[npi] = v[i] * chord
                }
            }
        }
/*        for (i in 0 until m) {
            print("i=$i, ")
            for (j in 0 until m) print("${aa[i][j]},")
            println("")
        }*/
        val ind = IntArray(m)
        ludcmp(m, aa, ind)
        ctrlPts.addAll(lubksb(m, aa, ind, bb))
    }

    //Algorithm 9.2
    private fun cubicTridiagonal() {
        /*
            val n = pts.size
            val nm1 = n - 1
            val bb = Array(3) {DoubleArray(n+2)}
            for (i in 3..nm1)
                for (j in 0..2) bb[j][i] = pts[i][j-1]
            val nn = basisFuncs(4,knots[4])
            var den = nn[1]
            ctrlPts[1] =
            */
        TODO()
    }
}
package geoModel

import linearAlgebra.Vector3
import linearAlgebra.binomialCoef

open class Nurbs: Bspline {

    /*  A Nurbs curve is defined by
        C(t) = Sum( Ni(t) * wi * Pi ) / Sum( Ni(t) * wi )
        where Pi are the control points, wi are the weights, and Ni(t) are the basis funcs defined on the knot vector */

    var wts = mutableListOf<Double>()

    constructor(): this(3)

    constructor(max: Int): super(max)

    constructor(max: Int, p: MutableList<Vector3>, w: MutableList<Double>): super(max, p) {
        this.wts = w
    }

    override fun addPts(v: Vector3) {
        super.addPts(v); wts.add(1.0)
    }

    override fun addPts(i: Int, v: Vector3) {
        super.addPts(i, v); wts.add(1.0)
    }

    override fun modPts(i: Int, v: Vector3) {
        super.modPts(i, v); wts[i] = 1.0
    }

    override fun removePts(i: Int) {
        super.removePts(i)
        if(i != -1) wts.removeAt(i)
    }

    //Algorithm 4.1 mod
    private fun denominator(span: Int, t: Double, ni: DoubleArray): Double {
        var sum = 0.0
        for (j in 0..degree)
            sum += wts[span - degree + j] * ni[j]
        return sum
    }

    //Algorithm 4.1 mod
    private fun numerator(span: Int, t: Double, ni: DoubleArray): Vector3 {
        var sum = Vector3().zero
        for (j in 0..degree)
            sum += ctrlPts[span - degree + j] * ni[j] * wts[span - degree + j]
        return sum
    }

    //Algorithm 4.1
    override fun curvePoint(t: Double): Vector3 {
        val span = findIndexSpan(ctrlPts.size, t)
        val ni = basisFuncs(span, t)
        return numerator(span, t, ni) / denominator(span, t, ni)
    }

    //Algorithm 4.2 mod
    override fun curveDers(kmax: Int, t: Double): Array<Vector3> {
        if(kmax == 0) return Array(1) { curvePoint(t) }
        // Compute kth derivatives
        val v = Array(kmax + 1) { Vector3() }
        val span = findIndexSpan(wts.size, t)
        val nders = dersBasisFunc(kmax, span, t)
        v[0] = curvePoint(t)
        for(k in 1..kmax) {
            v[k] = numerator(span, t, nders[k])
            for(j in 1..k) {
                v[k] -= v[k - j] * binomialCoef(k, j) * denominator(span, t, nders[j])
            }
            v[k] = v[k] / denominator(span, t, nders[0])
        }
        return v
    }
}
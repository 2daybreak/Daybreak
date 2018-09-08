package geoModel

import linearAlgebra.Vector3
import java.awt.Graphics2D

open class SurfaceBspline: SurfaceParametric, Bspline(3) {
    override fun closestSurfPoint(v: Vector3): Vector3 {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun invoke(v: Vector3): Vector3 {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun surfaceDers(kmax: Int, t1: Double, t2: Double): Array<Vector3> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun draw(g: Graphics2D) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /*  A B-Spline surface is defined by
    S(t1, t2) = Sum( Ni(t1) * Nj(t2) * Pij )
    where Pij are the control points, Ni(t1) & Nj(t2) are the basis funcs defined on the knot vector */

    final override var ctrlNet = mutableListOf<Array<Vector3>>()

    override fun addCvs(c: ParametricCurve) {
        ctrlNet.add(c.ctrlPts.toTypedArray())
    }

    override fun addCvs(i: Int, c: ParametricCurve) {
        ctrlNet.add(i, c.ctrlPts.toTypedArray())
    }

    override fun removeCvs(i: Int) {
        ctrlNet.removeAt(i)
    }

    override fun surfacePoint(t1: Double, t2: Double): Vector3 {
        val ispan = findIndexSpan(ctrlPts.size, t1)
        val ibasf = basisFuncs(ispan, t1)
        val jspan = findIndexSpan(ctrlPts.size, t2)
        val jbasf = basisFuncs(jspan, t2)

        var v = Vector3().zero
        for(i in 0..degree) {
            for(j in 0..degree)
            {
                v += ctrlPts[jspan - degree + j] * jbasf[j]
            }
            return v
        }
        TODO()
    }
}

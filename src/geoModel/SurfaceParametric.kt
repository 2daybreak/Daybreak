package geoModel

import linearAlgebra.Vector3
import java.awt.Graphics2D

interface SurfaceParametric {

    val ctrlNet: MutableList<Array<Vector3>>

    fun addCvs(c: ParametricCurve)

    fun addCvs(i: Int, c: ParametricCurve)

    fun removeCvs(i: Int)

    operator fun invoke(t1: Double, t2: Double): Vector3 {
        return surfacePoint(t1, t2)
    }

    operator fun invoke(kmax: Int, t1: Double, t2: Double): Array<Vector3> {
        return surfaceDers(kmax, t1, t2)
    }

    operator fun invoke(v: Vector3): Vector3 {
        return closestSurfPoint(v)
    }

    fun surfacePoint(t1: Double, t2: Double): Vector3

    fun surfaceDers(kmax: Int, t1: Double, t2: Double): Array<Vector3>

    fun closestSurfPoint(v: Vector3): Vector3

    fun draw(g: Graphics2D) {}
}
package geoModel

import linearAlgebra.Vector3
import java.awt.Graphics2D

interface Parametric {

    val prm: MutableList<Double>

    val ctrlPts: MutableList<Vector3>

    operator fun invoke(t: Double): Vector3

    operator fun invoke(kmax: Int, t: Double): Array<Vector3>

    operator fun invoke(v: Vector3): Vector3

    fun addPts(v: Vector3)

    fun addPts(i: Int, v: Vector3)

    fun modPts(i: Int, v: Vector3)

    fun removePts(i: Int)

    fun addSlope(i: Int, v: Vector3)

    fun modSlope(i: Int, v: Vector3)

    fun removeSlope(i: Int)

    fun distance(v: Vector3): Double

    fun split(t: Double)

    fun draw(g: Graphics2D)
}
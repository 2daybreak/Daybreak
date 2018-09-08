package geoModel

import linearAlgebra.Vector3
import kotlin.math.sqrt

class Circle(val center: Vector3, val dia: Double): Nurbs(3) {

    val rds = dia / 2

    init {
        degree = 2
        order = 3
        evalAll()
    }

    private fun evalAll() {

        knots.add(0.00); knots.add(0.00); knots.add(0.00)
        knots.add(0.25); knots.add(0.25)
        knots.add(0.50); knots.add(0.50)
        knots.add(0.75); knots.add(0.75)
        knots.add(1.00); knots.add(1.00); knots.add(1.00)

        wts.add(1.0); wts.add(sqrt(2.0)/2)
        wts.add(1.0); wts.add(sqrt(2.0)/2)
        wts.add(1.0); wts.add(sqrt(2.0)/2)
        wts.add(1.0); wts.add(sqrt(2.0)/2)
        wts.add(1.0)

        ctrlPts.add(center + Vector3(rds, 0.0, 0.0))
        ctrlPts.add(center + Vector3(rds, rds, 0.0))
        ctrlPts.add(center + Vector3(0.0, rds, 0.0))
        ctrlPts.add(center + Vector3(-rds, rds, 0.0))
        ctrlPts.add(center + Vector3(-rds, 0.0, 0.0))
        ctrlPts.add(center + Vector3(-rds, -rds, 0.0))
        ctrlPts.add(center + Vector3(0.0, -rds, 0.0))
        ctrlPts.add(center + Vector3(rds, -rds, 0.0))
        ctrlPts.add(center + Vector3(rds, 0.0, 0.0))
    }

}
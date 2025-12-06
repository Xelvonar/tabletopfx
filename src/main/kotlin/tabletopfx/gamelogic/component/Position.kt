package tabletopfx.gamelogic.component

import kotlin.math.sqrt

data class Position(val x: Double, val y: Double) {

    fun distanceTo(other: Position): Double =
        sqrt(
            ((x - other.x) * (x - other.x).toDouble()) +
                    ((y - other.y) * (y - other.y).toDouble())
        )
}
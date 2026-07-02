package tabletopfx.ui.location

import javafx.geometry.Point2D
import tabletopfx.gamelogic.component.Location
import tabletopfx.gamelogic.component.SquareGridLoc
import tabletopfx.gamelogic.component.UnknownLoc
import kotlin.math.floor

/**
 * ### Architectural Role: Rectangular Grid Coordinate Strategy
 * Implements high-precision 2D orthogonal matrix mathematics to track position mapping across standard
 * checkerboards, chess files, or tile layouts.
 *
 * @property originX Absolute X distance offset from the canvas left frame margin edge to the boundary start of column 0.
 * @property originY Absolute Y distance offset from the canvas top frame margin edge to the boundary start of row 0.
 * @property cellWidth The pixel bounding width constraint tracking individual square cells.
 * @property cellHeight The pixel bounding height constraint tracking individual square cells.
 * @property maxRows Maximum row threshold limit configured on the active board image map layer.
 * @property maxCols Maximum column threshold limit configured on the active board image map layer.
 * @see LocationTranslator
 * @see SquareGridLoc
 */
class SquareGridTranslator(
    private val originX: Double,
    private val originY: Double,
    private val cellWidth: Double,
    private val cellHeight: Double,
    private val maxRows: Int,
    private val maxCols: Int
) : LocationTranslator {

    /**
     * Computes the center visual layout positioning coordinates corresponding to a discrete grid coordinate index.
     */
    override fun toPixels(location: Location): Point2D {
        return when (location) {
            is SquareGridLoc -> {
                val x = originX + (location.col * cellWidth) + (cellWidth / 2.0)
                val y = originY + (location.row * cellHeight) + (cellHeight / 2.0)
                Point2D(x, y)
            }
            else -> throw IllegalArgumentException(
                "SquareGridTranslator translation abort: Target mismatch. Class token cannot map category type: ${location::class.simpleName}"
            )
        }
    }

    /**
     * Maps continuous pointer hit variables back onto verified integer matrix index steps.
     */
    override fun toLocation(pixel: Point2D): Location {
        val localX = pixel.x - originX
        val localY = pixel.y - originY

        val row = floor(localY / cellHeight).toInt()
        val col = floor(localX / cellWidth).toInt()

        val isValid = row in 0 until maxRows && col in 0 until maxCols

        return if (isValid) {
            SquareGridLoc(row = row, col = col)
        } else {
            UnknownLoc
        }
    }
}
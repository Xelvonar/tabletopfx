package tabletopfx.ui.controller

import javafx.geometry.Point2D
import javafx.scene.input.MouseEvent

/**
 * Universal contract for processing raw user pointer actions.
 */
interface GestureHandler {
    fun handlePressed(event: MouseEvent)
    fun handleDragged(event: MouseEvent)
    fun handleReleased(event: MouseEvent)
}

/**
 * An implementation of GestureHandler that evaluates screen pixels to differentiate
 * between a deliberate click vs. an intentional dragging motion.
 *
 * @param threshold The distance in pixels the cursor must travel before a drag locks in.
 * @param onDragStarted Invoked precisely once when the cursor breaches the threshold distance.
 * @param onDragContinued Invoked during subsequent movements while the drag remains captured.
 * @param onClicked Invoked upon release ONLY if a deliberate drag was never engaged.
 */
class SpatialGestureEngine(
    private val threshold: Double = 100.0,
    private val onDragStarted: (MouseEvent) -> Unit,
    private val onDragContinued: (MouseEvent) -> Unit = {},
    private val onDragFinished: (MouseEvent) -> Unit = {},
    private val onClicked: (MouseEvent) -> Unit
) : GestureHandler {

    private var anchor: Point2D? = null
    private var isDragging = false

    override fun handlePressed(event: MouseEvent) {
        anchor = javafx.geometry.Point2D(event.sceneX, event.sceneY)
        isDragging = false
    }

    override fun handleDragged(event: MouseEvent) {
        val startPoint = anchor ?: return

        if (isDragging) {
            // Continuous loop: Forward updates directly to onComponentDragged
            onDragContinued(event)
        } else {
            // Threshold check: Use scene coordinates so node layout shifts don't warp the math
            val currentPoint = javafx.geometry.Point2D(event.sceneX, event.sceneY)
            println(startPoint.distance(currentPoint))
            if (startPoint.distance(currentPoint) > threshold) {
                isDragging = true
                onDragStarted(event)
            }
        }
    }

    override fun handleReleased(event: MouseEvent) {
        if (isDragging) {
            onDragFinished(event)
        } else {
            onClicked(event)
        }

        anchor = null
        isDragging = false
    }
}
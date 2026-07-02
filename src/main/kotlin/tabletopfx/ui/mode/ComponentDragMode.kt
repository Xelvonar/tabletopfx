package tabletopfx.ui.mode

import javafx.geometry.Point2D
import javafx.scene.input.MouseEvent
import tabletopfx.gamelogic.command.MoveComponentCommand
import tabletopfx.ui.controller.UiController
import java.util.UUID

/**
 * ### Architectural Role: Ephemeral Displacement State
 * Coordinates real-time continuous tracking gestures, managing continuous visual modifications.
 * Updates physical scene position nodes during dragging and dispatches structural rule commands on release.
 *
 * @property draggedId Unique identifier matching the active piece target in motion.
 * @property anchorX Calculated absolute window alignment translation offset modifier variable.
 * @property anchorY Calculated absolute window alignment translation offset modifier variable.
 */
data class ComponentDragMode(
    val draggedId: UUID,
    val anchorX: Double,
    val anchorY: Double
) : UiMode {

    override fun onEnter(ctx: UiController) {
        super.onEnter(ctx)
        println("Entering Drag Mode")
    }

    override fun onExit(ctx: UiController) {
        super.onExit(ctx)
        println("Exiting Drag Mode")
    }

    override fun onComponentDragged(ctx: UiController, id: UUID, event: MouseEvent) {
        if (id != draggedId) return

        val componentLayer = ctx.view.getComponentLayer()
        componentLayer.updateComponentPosition(
            componentId = draggedId,
            x = event.sceneX - anchorX,
            y = event.sceneY - anchorY
        )
    }

    override fun onComponentReleased(ctx: UiController, id: UUID, event: MouseEvent) {
        if (id != draggedId) return

        val dropPoint = Point2D(event.sceneX, event.sceneY)
        val targetLocation = ctx.locationTranslator.toLocation(dropPoint)

        ctx.requestCommand(
            MoveComponentCommand(
                id = draggedId,
                endLocation = targetLocation
            )
        )

        ctx.clearSelection()
        event.consume()
        ctx.switchToMode(IdleMode)
    }
}
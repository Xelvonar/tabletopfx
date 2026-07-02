package tabletopfx.ui.mode

import javafx.geometry.Point2D
import javafx.scene.input.MouseEvent
import tabletopfx.ui.controller.UiController
import java.util.UUID

/**
 * ### Architectural Role: Contextual Entity Engagement Focus State
 * Activated immediately following an initial tap choice sequence over a valid item asset target.
 * Leverages the presentation tier to manipulate active selection model parameters and handle focus switches.
 *
 * @property selectedComponentId Reference key tracking the active focused component element [UUID].
 */
data class ComponentClickedMode(
    val selectedComponentId: UUID
) : UiMode {

    override fun onEnter(ctx: UiController) {
        super.onEnter(ctx)
        println("Entering Clicked Mode")
    }

    override fun onExit(ctx: UiController) {
        super.onExit(ctx)
        println("Exiting Clicked Mode")
    }

    override fun onBoardClicked(ctx: UiController, point: Point2D, event: MouseEvent) {
        ctx.clearSelection()
        ctx.switchToMode(IdleMode)
    }

    override fun onComponentClicked(ctx: UiController, id: UUID, event: MouseEvent) {
        if (id == selectedComponentId) {
            ctx.clearSelection()
            ctx.switchToMode(IdleMode)
        } else {
            ctx.selectComponent(id)
            ctx.switchToMode(ComponentClickedMode(id))
        }
    }
}
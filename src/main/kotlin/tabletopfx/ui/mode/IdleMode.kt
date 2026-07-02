package tabletopfx.ui.mode

import javafx.scene.input.MouseEvent
import tabletopfx.ui.controller.UiController
import java.util.UUID

/**
 * ### Architectural Role: Default Resting State
 * Deployed whenever the workspace landscape sits completely un-engaged.
 * Monitors filtered vector fact pulses to swap contexts or prepare dragging states.
 *
 * @see ComponentDragMode
 * @see ComponentClickedMode
 */
object IdleMode : UiMode {
    override fun onEnter(ctx: UiController) {
        super.onEnter(ctx)
        println("Entering IdleMode")
    }

    override fun onExit(ctx: UiController) {
        super.onExit(ctx)
        println("Exiting IdleMode")
    }

    override fun onComponentClicked(ctx: UiController, id: UUID, event: MouseEvent) {
        ctx.selectComponent(id)
        ctx.switchToMode(ComponentClickedMode(id))
    }

    override fun onComponentDragStarted(ctx: UiController, id: UUID, event: MouseEvent) {
        ctx.selectComponent(id)

        val node = ctx.view.getComponentLayer().getComponentNode(id)
        val currentLayoutX = node?.layoutX ?: 0.0
        val currentLayoutY = node?.layoutY ?: 0.0

        ctx.switchToMode(
            ComponentDragMode(
                draggedId = id,
                anchorX = event.sceneX - currentLayoutX,
                anchorY = event.sceneY - currentLayoutY
            )
        )
    }
}
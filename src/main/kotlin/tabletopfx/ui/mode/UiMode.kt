package tabletopfx.ui.mode

import javafx.geometry.Point2D
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseEvent
import tabletopfx.ui.controller.UiController
import java.util.UUID

/**
 * ### Architectural Role: Abstract Input Capture State (Finite State Machine Contract)
 * Establishes the uniform type boundary matching behavioral interaction profiles inside the view tier.
 * By breaking complex input paths into distinct contextual classes, the system eliminates traditional,
 * chaotic procedural state testing flag blocks. Each state operates as an isolated,
 * self-contained routing matrix handling a small subset of events.
 *
 * This contract supports stabilized, higher-level interaction choices
 * processed via the component-level geometry engine pipeline.
 *
 * @see IdleMode
 * @see ComponentClickedMode
 * @see ComponentDragMode
 */
sealed interface UiMode {

    /**
     * Triggered immediately when this specific interaction mode is pushed onto the controller pipeline.
     */
    fun onEnter(ctx: UiController) {}

    /**
     * Triggered immediately before this mode is detached and replaced by an upcoming tracking state.
     */
    fun onExit(ctx: UiController) {}

    /**
     * Standard escape route gateway execution block.
     * Invoked when users hit right-click or press the ESC key to discard uncommitted actions.
     */
    fun onCancel(ctx: UiController) {
        ctx.switchToMode(IdleMode)
    }

    // --- Interactive Mouse Event Hooks ---
    fun onBackgroundClick(ctx: UiController, event: MouseEvent) {}
    fun onBackgroundMove(ctx: UiController, event: MouseEvent) {}

    /**
     * Invoked precisely when a component finishes a deliberate selection action, as filtered
     * and confirmed by its individual geometric state-machine tracking system.
     */
    fun onComponentClicked(ctx: UiController, id: UUID, event: MouseEvent) {}

    /**
     * Invoked precisely when a component breaks its noise window threshold, transitioning from an armed state
     * to a confirmed, active dragging pipeline gesture.
     */
    fun onComponentDragStarted(ctx: UiController, id: UUID, event: MouseEvent) {}

    /**
     * Invoked iteratively during an ongoing, active tracking drag update as long as mouse tracking remains unreleased.
     */
    fun onComponentDragged(ctx: UiController, id: UUID, event: MouseEvent) {}

    /**
     * Invoked precisely when a component tracking drag layout loop drops active coordinate execution on mouse release.
     */
    fun onComponentReleased(ctx: UiController, id: UUID, event: MouseEvent) {}

    fun onComponentLayerClick(ctx: UiController, event: MouseEvent) {}

    fun onGridClick(ctx: UiController, event: MouseEvent) {}
    fun onGridMove(ctx: UiController, event: MouseEvent) {}

    fun onUiLayerClick(ctx: UiController, event: MouseEvent) {}
    fun onUiLayerMove(ctx: UiController, event: MouseEvent) {}

    fun onBoardClicked(ctx: UiController, point: Point2D, event: MouseEvent) {}
    fun onBoardPressed(ctx: UiController, point: Point2D, event: MouseEvent) {}
    fun onBoardDragged(ctx: UiController, point: Point2D, event: MouseEvent) {}
    fun onBoardReleased(ctx: UiController, point: Point2D, event: MouseEvent) {}

    // --- Interactive Keyboard Hook ---
    fun onKeyPressed(ctx: UiController, event: KeyEvent) {}
}
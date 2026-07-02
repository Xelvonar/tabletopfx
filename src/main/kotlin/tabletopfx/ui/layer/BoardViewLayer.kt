package tabletopfx.ui.layer

import javafx.scene.layout.Pane

/**
 * ### Architectural Role: Stacked Presentation Node Primitive
 * Establishes the core layout base abstraction for all distinct visual layers composited inside the board window.
 * * Extends JavaFX's standard hardware-accelerated continuous coordinate [Pane] grid field, defaulting
 * mouse picking traits tightly to prevent empty canvas sectors from swallowing pointer ticks needed by
 * background surfaces beneath them.
 *
 * @see BackgroundLayer
 * @see ComponentLayer
 * @see GridLayer
 * @see UiLayer
 */
abstract class BoardViewLayer : Pane() {

    init {
        styleClass.add("board-layer")

        // Invariant: Non-filled canvas spaces allow events to naturally fall through to layers stacked beneath
        isPickOnBounds = false
    }

    /**
     * Purges all active child scene-graph nodes compiled inside this visual layer.
     */
    open fun clear() {
        children.clear()
    }

    /**
     * Imperatively configures whether this entire surface plane acts transparently against pointer inputs.
     *
     * @param transparent If true, mouse click interactions ignore this pane and fall straight down the stack.
     */
    fun setLayerMouseTransparent(transparent: Boolean) {
        this.isMouseTransparent = transparent
    }
}
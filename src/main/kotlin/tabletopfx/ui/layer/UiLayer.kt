package tabletopfx.ui.layer

import tabletopfx.ui.selection.SelectionOverlay

/**
 * ### Architectural Role: Phantom Transient FX HUD Sheet (The Glass Overlay)
 * Composes the uppermost tactical layer sheet deployed inside the viewport pane setup.
 * * Implements mouse-transparency to allow continuous clicking streams to hit token cards or board targets.
 * This surface specializes strictly in displaying temporary user selections, canvas highlights, ghost token drag shapes,
 * measuring tapes, or contextual range finder vectors.
 */
class UiLayer : BoardViewLayer() {

    init {
        styleClass.add("ui-layer")

        // Enforce absolute non-blocking mouse behavior over the visual feedback landscape pane
        this.isMouseTransparent = true
    }

    /**
     * Mounts a selection box panel vector canvas, scaling bounds onto target boundaries.
     *
     * @param overlay Concrete tracking pane element handling selection marquee borders.
     */
    fun installOverlay(overlay: SelectionOverlay) {
        children.add(overlay)

        // Bind dimension properties to follow container geometry pulses dynamically
        overlay.prefWidthProperty().bind(widthProperty())
        overlay.prefHeightProperty().bind(heightProperty())

        overlay.isMouseTransparent = true
    }
}
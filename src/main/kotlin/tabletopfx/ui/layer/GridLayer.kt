package tabletopfx.ui.layer

/**
 * ### Architectural Role: Spatial Regulation Mesh Plane (The Vector Grid Overlay)
 * Sits directly between the base [BackgroundLayer] illustration and active piece tokens.
 * * This layer composites regular square grids, tactical hex geometry guidelines, or geometric vector outlines.
 * Because it preserves bounds-checking transparency, background images show through the cells smoothly.
 */
class GridLayer : BoardViewLayer() {

    init {
        styleClass.add("grid-layer")
    }

    /**
     * Toggles the rendering state of the vector overlay guidelines.
     */
    fun setGridVisible(visible: Boolean) {
        this.isVisible = visible
    }
}
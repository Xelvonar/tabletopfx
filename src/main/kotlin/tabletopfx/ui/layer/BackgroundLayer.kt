package tabletopfx.ui.layer

import javafx.scene.image.Image
import javafx.scene.image.ImageView

/**
 * ### Architectural Role: Master Layout Canvas Raster (The Game Table Surface)
 * Sits at the absolute base bottom position of the board view layer composition sequence stack.
 * * This concrete layer hosts the root map illustrations, table graphics, or primary game boards.
 * Because it serves as the ultimate backdrop plane, it sets picking flags to guarantee that clicks landing
 * entirely clear of pieces or grid borders are absorbed here to clear state machine selections.
 */
class BackgroundLayer : BoardViewLayer() {

    private val imageView = ImageView()

    init {
        styleClass.add("background-layer")

        // Enforce boundary click absorption on empty sectors to prevent desktop pass-through leakage
        isPickOnBounds = true

        // Establish strict dimensional bindings to stay scaled against window resize pulses dynamically
        imageView.fitWidthProperty().bind(widthProperty())
        imageView.fitHeightProperty().bind(heightProperty())

        children.add(imageView)
    }

    /**
     * Mounts a compiled graphic resource texture map asset onto the underlying board target frame.
     *
     * @param img Pre-loaded [Image] asset reference data representing the active map (e.g., Chess_board.png).
     */
    fun setMapImage(img: Image?) {
        imageView.image = img
    }
}
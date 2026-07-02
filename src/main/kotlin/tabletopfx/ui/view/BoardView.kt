package tabletopfx.ui.view

import javafx.geometry.Point2D
import javafx.scene.image.Image
import javafx.scene.input.MouseEvent
import javafx.scene.layout.StackPane
import tabletopfx.ui.layer.BackgroundLayer
import tabletopfx.ui.layer.ComponentLayer
import tabletopfx.ui.layer.GridLayer
import tabletopfx.ui.layer.UiLayer

/**
 * ### Architectural Role: Unified Multi-Layer Presentation Viewport Canvas
 * Serves as the primary parent screen container layout for the tabletop viewport grid space.
 * * Extends JavaFX's standard [StackPane] layout to handle sequential layering bounds. This composite view
 * coordinates zero game rules, selection filters, or mapping states; it aggregates layout canvases
 * from bottom to top and forwards raw hardware capture events up to your interaction states.
 *
 * | Stack Order Layer | Concrete Subclass | Contextual Operational Responsibility |
 * | :--- | :--- | :--- |
 * | 4: Uppermost Plane | [UiLayer] | Renders selection highlights, range circles, and preview nodes. |
 * | 3: Interaction Plane | [ComponentLayer] | Maps live piece textures, processing click captures. |
 * | 2: Grid Guide Plane | [GridLayer] | Render tactical layout grid matrices (Squares/Hexes). |
 * | 1: Absolute Base | [BackgroundLayer] | Hosts the main battlefield table asset (e.g. Chess_board.png). |
 *
 * @see BoardViewLayer
 * @see tabletopfx.ui.controller.UiController
 */
class BoardView : StackPane() {

    private val bgLayer = BackgroundLayer()
    private val gridLayer = GridLayer()
    private val componentLayer = ComponentLayer()
    private val uiLayer = UiLayer()

    // --- Background Raw Hardware Interaction Callbacks ---
    var onBoardPressed: ((Point2D, MouseEvent) -> Unit)? = null
    var onBoardDragged: ((Point2D, MouseEvent) -> Unit)? = null
    var onBoardReleased: ((Point2D, MouseEvent) -> Unit)? = null

    init {
        // Compose application workspace layers sequentially (Bottom-to-Top composition alignment)
        children.addAll(bgLayer, gridLayer, componentLayer, uiLayer)

        setupBackgroundInteractions()
    }

    /**
     * Captures mouse interactions that manage to clear or fall through foreground components.
     */
    private fun setupBackgroundInteractions() {
        setOnMousePressed { event ->
            if (!event.isConsumed) {
                // Utilizing safe absolute scene spaces to guarantee stable spatial alignment math
                onBoardPressed?.invoke(Point2D(event.sceneX, event.sceneY), event)
            }
        }

        setOnMouseDragged { event ->
            if (!event.isConsumed) {
                onBoardDragged?.invoke(Point2D(event.sceneX, event.sceneY), event)
            }
        }

        setOnMouseReleased { event ->
            if (!event.isConsumed) {
                onBoardReleased?.invoke(Point2D(event.sceneX, event.sceneY), event)
            }
        }
    }

    /**
     * Injects the background image representing the board table.
     *
     * @param resourcePath Classpath file location pointing to the layout asset (e.g., "/images/Chess_board.png").
     */
    fun setBackgroundImage(resourcePath: String) {
        val stream = javaClass.getResourceAsStream(resourcePath)
            ?: throw IllegalArgumentException("Graphic file missing: Map illustration asset not recovered at location: $resourcePath")
        bgLayer.setMapImage(Image(stream))
    }

    /**
     * Exposes the type-safe [ComponentLayer] context to let orchestrators bind entity state visualizers.
     */
    fun getComponentLayer(): ComponentLayer = componentLayer

    /**
     * Exposes the type-safe [UiLayer] context to let orchestrators attach visual overlays.
     */
    fun getUiLayer(): UiLayer = uiLayer
}
package tabletopfx.ui.layer

import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent
import tabletopfx.gamelogic.component.Component
import tabletopfx.gamelogic.property.ImageProp
import tabletopfx.ui.controller.SpatialGestureEngine
import java.util.UUID

/**
 * ### Architectural Role: Read-Path Entity Sprite Composite Layer
 * Coordinates the visual instantiation, removal, translation, and rendering loops tracking game items on screen.
 * * This layer bridges business objects to concrete graphic assets by mapping individual logical models
 * straight into hardware-accelerated [ImageView] containers keyed by their invariant domain [UUID]. It hooks
 * up input capture conduits to bubble physical mouse actions straight into the active controller.
 */
open class ComponentLayer : BoardViewLayer() {

    private val _componentNodes = HashMap<UUID, ImageView>()

    /**
     * Read-only mirror ledger tracking compiled on-screen token nodes in active scope.
     */
    val componentNodes: Map<UUID, ImageView>
        get() = _componentNodes

    private var clickHandler: ((UUID, MouseEvent) -> Unit)? = null
    private var dragHandler: ((UUID, MouseEvent) -> Unit)? = null

    // Component mouse interaction exposed to be hooked into by UiModes.
    var onComponentClicked: ((UUID, MouseEvent) -> Unit)? = null
    var onComponentDragStarted: ((UUID, MouseEvent) -> Unit)? = null
    var onComponentDragContinued: ((UUID, MouseEvent) -> Unit)? = null
    var onComponentDragReleased: ((UUID, MouseEvent) -> Unit)? = null

    init {
        styleClass.add("component-layer")
    }

    /**
     * Inspects active scene graph collections to extract a target piece graphic reference wrapper.
     */
    fun getComponentNode(componentId: UUID): ImageView? {
        return _componentNodes[componentId]
    }

    /**
     * Spawns or updates a piece's visible texture token on the display field.
     * Consumes events internally to protect the pipeline against coordinate leakage.
     *
     * @param pc Fresh immutable snapshot tracking the state variables of the target entity.
     */
    fun addComponent(pc: Component) {
        if (_componentNodes.containsKey(pc.id)) {
            removeComponent(pc.id)
        }

        val gestureEngine = SpatialGestureEngine(
            threshold = 5.0,
            onDragStarted = { event ->  onComponentDragStarted?.invoke(pc.id, event) },
            onDragContinued = { event -> onComponentDragContinued?.invoke(pc.id, event) },
            onDragFinished = { event -> onComponentDragReleased?.invoke(pc.id, event) },
            onClicked = { event -> onComponentClicked?.invoke(pc.id, event) }
        )

        val node = createNodeForComponent(pc).apply {
            userData = pc.id

            setOnMousePressed { e -> gestureEngine.handlePressed(e); e.consume() }
            setOnMouseDragged { e -> gestureEngine.handleDragged(e); e.consume() }
            setOnMouseReleased { e -> gestureEngine.handleReleased(e); e.consume() }
        }

        children.add(node)
        _componentNodes[pc.id] = node
    }

    /**
     * Low-level manufacturing factory pulling streamed asset bundles out of application paths.
     */
    protected open fun createNodeForComponent(pc: Component): ImageView {
        val state = pc.aspects[pc.currentAspectKey]
            ?: throw IllegalStateException("Component model context error: Aspect '${pc.currentAspectKey}' not configured.")

        val imageProp = state.properties.values.filterIsInstance<ImageProp>().firstOrNull()
            ?: throw IllegalArgumentException("Component asset lookup violation: Target configuration missing visible ImageProp details.")

        // FIX: Extract the internal string field parameter variable reference explicitly to build path keys
        val imagePath = "/images/${imageProp.imageRef}"
        val stream = javaClass.getResourceAsStream(imagePath)
            ?: throw IllegalArgumentException("Resource storage breakdown: Target stream key bundle not recovered at path: $imagePath")

        return ImageView(Image(stream)).apply {
            layoutX = 0.0
            layoutY = 0.0
            isPickOnBounds = true
            id = "component-${pc.id}"
        }
    }

    /**
     * Erases a component's visible reference frame out of the runtime scene tree graphics ledger.
     */
    fun removeComponent(componentId: UUID) {
        _componentNodes.remove(componentId)?.let { node ->
            children.remove(node)
        }
    }

    /**
     * Shifts double layout bounds positions to track updates without recreating nodes.
     */
    fun updateComponentPosition(componentId: UUID, x: Double, y: Double) {
        _componentNodes[componentId]?.let { node ->
            val halfWidth = node.boundsInLocal.width / 2.0
            val halfHeight = node.boundsInLocal.height / 2.0

            node.layoutX = x - halfWidth
            node.layoutY = y - halfHeight

            // 2. Debug Print properties out to the terminal log
//            val local = node.boundsInLocal
//            val parent = node.boundsInParent
//
//            println("""
//            ===================================================================
//            [DEBUG HITBOX] Component ID: $componentId
//            -------------------------------------------------------------------
//            • Intended Target Layout Coordinates : X=$x, Y=$y
//            • Node Inner Texture Size (Local)    : Width=${local.width}, Height=${local.height}
//            • Screen Footprint Region (Parent)   : MinX=${parent.minX}, MinY=${parent.minY}
//                                                   MaxX=${parent.maxX}, MaxY=${parent.maxY}
//            ===================================================================
//        """.trimIndent())
        }
    }

    /**
     * Toggles a sprite node's visibility flag safely.
     */
    fun setComponentVisible(componentId: UUID, visible: Boolean) {
        _componentNodes[componentId]?.isVisible = visible
    }

    /**
     * Flushes the entire graphic ledger tree down to baseline values.
     */
    fun clearComponents() {
        children.removeAll(_componentNodes.values)
        _componentNodes.clear()
    }

    // --- Interaction Routing Registration Conduits ---
    fun setComponentClickedHandler(handler: (UUID, MouseEvent) -> Unit) {
        this.clickHandler = handler
    }

    fun setComponentDraggedHandler(handler: (UUID, MouseEvent) -> Unit) {
        this.dragHandler = handler
    }
}

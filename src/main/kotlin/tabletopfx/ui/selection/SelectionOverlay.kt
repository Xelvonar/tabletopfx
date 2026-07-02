package tabletopfx.ui.selection

import javafx.beans.value.ChangeListener
import javafx.collections.SetChangeListener
import javafx.geometry.Bounds
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.image.ImageView
import java.util.UUID

/**
 * ### Architectural Role: Reactive Presentation Highlight Overlay
 * A transparent visual layer stacked directly inside the upper HUD tier canvas pane hierarchy.
 * * It monitors changes inside the [SelectionModel] reactively. When an identity token is added or cleared,
 * it intercepts the notification, extracts the physical coordinate matrix out of the provided [ImageView] map registry,
 * and paints localized highlight vectors cleanly over the targeted asset.
 *
 * It manages strict event containment rules, enabling continuous mouse clicks to fall through its plane unimpeded
 * to touch pieces or background tiles beneath it.
 *
 * @see SelectionModel
 * @see tabletopfx.ui.layer.UiLayer
 */
class SelectionOverlay(
    private val selectionModel: SelectionModel,
    private val nodeRegistry: Map<UUID, ImageView>
) : Pane() {

    /**
     * Caches dynamic coordinate listener objects paired with custom display vector elements.
     * Essential for tracking allocations to prevent background reference leaks on teardown.
     */
    private data class HighlightContext(
        val visual: javafx.scene.Node,
        val boundsListener: ChangeListener<Bounds>
    )

    private val activeHighlights = HashMap<UUID, HighlightContext>()

    init {
        // Enforce absolute non-blocking event behavior over the layout layer boundary
        isMouseTransparent = true

        // Register reactive state listener traps straight onto the observable model loop
        selectionModel.selectedIds.addListener(SetChangeListener { change ->
            if (change.wasAdded()) {
                createHighlight(change.elementAdded)
            }
            if (change.wasRemoved()) {
                removeHighlight(change.elementRemoved)
            }
        })
    }

    /**
     * Generates a structural selection marquee frame tracking the targeted piece node bounds.
     */
    private fun createHighlight(id: UUID) {
        val targetNode = nodeRegistry[id] ?: return

        val rect = Rectangle().apply {
            fill = Color.TRANSPARENT
            stroke = Color.CYAN
            strokeWidth = 3.0
            effect = javafx.scene.effect.GaussianBlur(2.0)
        }

        // Translation calculation logic: Maps coordinate bounds relative to this pane canvas sheet
        val updatePosition = {
            val localBounds = targetNode.boundsInLocal
            val sceneBounds = targetNode.localToScene(localBounds)
            val overlayBounds = this.sceneToLocal(sceneBounds)

            rect.x = overlayBounds.minX
            rect.y = overlayBounds.minY
            rect.width = overlayBounds.width
            rect.height = overlayBounds.height
        }

        // Synchronize initial rendering coordinates
        updatePosition()

        // Bind an operational position tracker lambda to follow real-time coordinate mutations seamlessly
        val boundsListener = ChangeListener<Bounds> { _, _, _ ->
            updatePosition()
        }

        targetNode.boundsInParentProperty().addListener(boundsListener)

        activeHighlights[id] = HighlightContext(rect, boundsListener)
        children.add(rect)
    }

    /**
     * Defensive Lifecycle Cleanup Path
     * Clears visual highlighting artifacts and tears down active property listener lookups.
     * This method acts as a critical garbage collection gateway, un-chaining the observer context
     * from long-lived token nodes to block memory leakage loops.
     */
    private fun removeHighlight(id: UUID) {
        val context = activeHighlights.remove(id) ?: return
        children.remove(context.visual)

        val targetNode = nodeRegistry[id]
        targetNode?.boundsInParentProperty()?.removeListener(context.boundsListener)
    }
}
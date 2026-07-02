package tabletopfx.ui.renderer

import javafx.application.Platform
import javafx.geometry.Point2D
import tabletopfx.gamelogic.event.GameEvent.ComponentMovedEvent
import tabletopfx.gamelogic.event.GameEvent.ComponentCreatedEvent // Added import
import tabletopfx.gamelogic.event.GameEventBus
import tabletopfx.ui.view.BoardView
import tabletopfx.ui.controller.UiController
import tabletopfx.ui.location.LocationTranslator

/**
 * ### Architectural Role: Read-Path Visual State Renderer (CQRS Synchronization Conduit)
 * Monitors the authoritative logic thread synchronously via the event bus to apply real-time presentation updates.
 */
class ComponentRenderer {

    /**
     * Mounts permanent event observation loops onto the type-safe event bus pipeline highway.
     */
    fun register(bus: GameEventBus, view: BoardView, ctx: UiController) {
        val layer = view.getComponentLayer()

        // 1. LISTEN: Intercept component movement events
        bus.subscribe<ComponentMovedEvent> { event ->
            val translator: LocationTranslator = ctx.locationTranslator
            val pixelPoint: Point2D = translator.toPixels(event.location)

            Platform.runLater {
                layer.updateComponentPosition(
                    componentId = event.id,
                    x = pixelPoint.x,
                    y = pixelPoint.y
                )
            }
        }

        // 2. LISTEN: Intercept component creation events
        bus.subscribe<ComponentCreatedEvent> { event ->
            // Grab spatial translator rules
            val translator: LocationTranslator = ctx.locationTranslator

            // Map abstract domain indices to double grid values
            val pixelPoint: Point2D = translator.toPixels(event.component.location)

            // Thread off to JavaFX to instantiate the node
            Platform.runLater {
                // Assuming your component layer has an insertion hook like create/addComponent:
                layer.addComponent(event.component)
                layer.updateComponentPosition(event.component.id,pixelPoint.x,pixelPoint.y)
            }
        }
    }
}
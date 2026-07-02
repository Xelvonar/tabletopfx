package tabletopfx.gamelogic.state

import tabletopfx.gamelogic.component.Aspect
import tabletopfx.gamelogic.component.Component
import tabletopfx.gamelogic.event.GameEvent
import tabletopfx.gamelogic.property.ImageProp

/**
 * ### Architectural Role: Component State Reduction Pipeline
 * An isolated workspace file implementing clean extension logic on top of the primary [GameState].
 * It isolates bulk procedural state copy manipulation code out of your clean primitive class maps.
 *
 * @param event The target lifecycle event fact containing execution payload records.
 * @return An altered clone of the master state tree containing the updated component registries.
 */
fun GameState.reduceComponentEvent(event: GameEvent): GameState {
    return when (event) {
        is GameEvent.ComponentCreatedEvent -> {
            this.copy(components = this.components + (event.component.id to event.component))
        }

        is GameEvent.ComponentMovedEvent -> {
            val target = this.components[event.id] ?: return this
            val updated = target.withLocation(event.location)

            this.copy(components = this.components + (event.id to updated))
        }

        else -> this
    }
}
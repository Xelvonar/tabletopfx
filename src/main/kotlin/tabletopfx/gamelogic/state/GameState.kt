package tabletopfx.gamelogic.state

import tabletopfx.gamelogic.component.Component
import tabletopfx.gamelogic.event.GameEvent
import tabletopfx.gamelogic.property.BoolProp
import tabletopfx.gamelogic.property.DoubleProp
import tabletopfx.gamelogic.property.ImageProp
import tabletopfx.gamelogic.property.IntProp
import tabletopfx.gamelogic.property.Property
import tabletopfx.gamelogic.property.StringProp
import java.util.UUID

/**
 * ### Architectural Role: Authoritative State Snapshot (The Single Source of Truth)
 * Represents an entirely immutable data snapshot of the active board state, global properties,
 * and registered component entities.
 *
 * State mutation is achieved using pure functional programming via the [transition] function,
 * which outputs a new [GameState] milestone whenever an event passes validation checks.
 *
 * @property stateId Monotonically increasing database revision index tracking historical timeline steps.
 * @property components Primary lookup map indexing active game pieces on the board by their [UUID].
 * @property data Ledger tracking top-level global game metadata values or configuration properties.
 * @see Component
 * @see GameEvent
 */
data class GameState(
    val stateId: Long = 0L,
    val components: Map<UUID, Component> = emptyMap(),
    val data: Map<String, Property> = emptyMap(),
) {

    /**
     * Safely reads and unboxes a top-level global property parameter value out of the master state dictionary.
     *
     * @param name The mapping key token being queried.
     * @return The raw unpacked inner value type primitive, or null if the field layout is missing.
     */
    fun get(name: String): Any? {
        return when (val prop = data[name]) {
            is IntProp -> prop.value
            is DoubleProp -> prop.value
            is BoolProp -> prop.value
            is StringProp -> prop.value
            is ImageProp -> prop.imageRef
            null          -> null
        }
    }

    /**
     * State Machine Transition Loop (The Reducer Traffic Cop)
     * Evolves the global state tree by matching the incoming event facts.
     *
     * @param event The verified structural domain fact published out by a command handler.
     * @return A pristine [GameState] copy reflecting the completed structural updates.
     */
    fun transition(event: GameEvent): GameState {
        // Automatically pre-increment the history lifecycle step version index
        val incrementedState = this.copy(stateId = this.stateId + 1)

        // FIX: Added explicit type tracking checks for every subclass to preserve exhaustiveness
        return when (event) {
            is GameEvent.ComponentCreatedEvent,
            is GameEvent.ComponentMovedEvent -> incrementedState.reduceComponentEvent(event)

            // Logging and Meta notifications pass straight through without mutating entities
            is GameEvent.MessageLoggedEvent,
            is GameEvent.GameStateChanged -> incrementedState
        }
    }
}
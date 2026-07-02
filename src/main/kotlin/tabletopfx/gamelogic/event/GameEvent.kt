package tabletopfx.gamelogic.event

import tabletopfx.gamelogic.component.Component
import tabletopfx.gamelogic.state.GameState
import tabletopfx.gamelogic.component.Location
import java.util.UUID

/**
 * ### Architectural Role: Unified Domain Event Primitives
 * Serves as the base supertype representing un-tamperable factual occurrences inside the game.
 * Events are historic payloads broadcasted over the shared bus after a command has successfully mutated
 * an authoritative snapshot.
 *
 * @see ComponentCreatedEvent
 * @see ComponentMovedEvent
 * @see MessageLoggedEvent
 * @see GameStateChanged
 */
sealed class GameEvent {

    /**
     * Broadcasts the structural creation and initial mapping of a component instance.
     *
     * @property id Unique domain identifier assigned to the new element.
     * @property name Human-readable name attached to the entity.
     * @property location Initial logical grid or layout field assignment destination.
     * @property imageRef Fallback or initial raw image texture reference string.
     * @property initialAspectName The identifier key tracking which aspect state is active at birth.
     */
    data class ComponentCreatedEvent(
        val component: Component,
    ) : GameEvent()

    /**
     * Broadcasts a definitive update delta tracking an entry shifting logic quadrants.
     *
     * @property id Unique domain identifier matching the active component node.
     * @property location Resolved terminal destination area where the entity landed.
     */
    data class ComponentMovedEvent(
        val id: UUID,
        val location: Location,
    ) : GameEvent()

    /**
     * System log transport broadcast container.
     * Pushes system diagnostics, notification warnings, or rule failure text logs to connected monitors.
     *
     * @property text Consolidated raw feedback characters to display on output panels.
     */
    data class MessageLoggedEvent(val text: String) : GameEvent()

    /**
     * Macroscopic data loop wrap-around event.
     * Delivers a full read-only snapshot pointing to a newly processed state milestone, forcing full tree layouts
     * or state caches to balance values accurately.
     *
     * @property state Fresh authoritative data map mirror instance.
     */
    data class GameStateChanged(val state: GameState) : GameEvent()
}
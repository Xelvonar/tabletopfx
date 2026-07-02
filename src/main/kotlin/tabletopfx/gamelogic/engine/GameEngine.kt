package tabletopfx.gamelogic.engine

import tabletopfx.gamelogic.command.GameCommand
import tabletopfx.gamelogic.command.handler.CommandHandler
import tabletopfx.gamelogic.event.GameEvent
import tabletopfx.gamelogic.event.GameEventBus
import tabletopfx.gamelogic.state.GameState
import java.util.ArrayList
import kotlin.reflect.KClass

/**
 * ### Architectural Role: Authoritative Transaction Engine (The Command Ringleader)
 * Coordinates and orchestrates the absolute lifecycle loop of the tabletop sandbox logic tier.
 * The engine captures incoming user intents, resolves the appropriate rule handler, runs validation blocks,
 * evolves the state snapshot, and broadcasts historic facts out to the read path.
 *
 * It acts as a linear timeline ledger, persisting full immutability history bounds to support native,
 * performance-clean undo and redo operations out of the box.
 *
 * @property eventBus Type-safe message bus channel used to broadcast transaction metrics and state revisions.
 * @see GameState
 * @see GameCommand
 * @see CommandHandler
 */
class GameEngine(
    initialState: GameState,
    val eventBus: GameEventBus,
    private val commandRegistry: Map<KClass<out GameCommand>, CommandHandler<out GameCommand>>
) {

    private val stateHistory = ArrayList<GameState>()
    private var currentStateIndex = 0

    /**
     * Resolves the current live milestone snapshot pointing to the active step in our timeline history.
     */
    val currentState: GameState
        get() = stateHistory[currentStateIndex]

    init {
        stateHistory.add(initialState)
    }

    /**
     * Primary Transaction Gateway (The Pipeline)
     * Ingests a customer intent envelope, processes it sequentially, and advances the game universe timeline.
     *
     * @param command Inbound structural intent payload package to execute.
     */
    @Suppress("UNCHECKED_CAST")
    fun request(command: GameCommand) {
        // 1. RESOLVE HANDLER: Map the class type to its respective rule validator instance
        val handler = commandRegistry[command::class] as? CommandHandler<GameCommand> ?: run {
            eventBus.publish(GameEvent.MessageLoggedEvent("System Error: No handler registered for command variant ${command::class.simpleName}"))
            return
        }

        // 2. VALIDATION GATEWAY: Halt transaction processing instantly if rules are violated
        if (!handler.validate(command, currentState)) {
            eventBus.publish(GameEvent.MessageLoggedEvent("Validation Failed: ${command::class.simpleName} rejected by rule constraints."))
            return
        }

        // 3. TIMELINE TRUNCATION: If writing a new action after an undo click, slice off the abandoned future timeline branches
        if (currentStateIndex < stateHistory.size - 1) {
            stateHistory.subList(currentStateIndex + 1, stateHistory.size).clear()
        }

        // 4. RULE EXECUTION: Evaluate variables to generate the permanent historical fact record
        val event = handler.execute(command, currentState)

        // 5. STATE REDUCTION: Pass the event data back to the functional snapshot reducer traffic cop
        val nextState = currentState.transition(event)

        // 6. HISTORICAL COMMIT: Mount the fresh snapshot copy cleanly into the timeline cache
        stateHistory.add(nextState)
        currentStateIndex++

        // 7. FACT BROADCASTS: Alert animations and layout binders synchronously over our highway
        eventBus.publish(event)
        eventBus.publish(GameEvent.GameStateChanged(currentState))
    }

    /**
     * Shifts the active history index pointer backward one milestone step to apply a visual state rollback.
     */
    fun undo() {
        if (currentStateIndex <= 0) {
            eventBus.publish(GameEvent.MessageLoggedEvent("Cannot Undo: Already at initial state checkpoint."))
            return
        }
        currentStateIndex--
        eventBus.publish(GameEvent.MessageLoggedEvent("Undid last action successfully."))
        eventBus.publish(GameEvent.GameStateChanged(currentState))
    }

    /**
     * Shifts the active history index pointer forward one milestone step to re-apply an undone action.
     */
    fun redo() {
        if (currentStateIndex >= stateHistory.size - 1) {
            eventBus.publish(GameEvent.MessageLoggedEvent("Cannot Redo: Already at latest timeline checkpoint."))
            return
        }
        currentStateIndex++
        eventBus.publish(GameEvent.MessageLoggedEvent("Redid last action successfully."))
        eventBus.publish(GameEvent.GameStateChanged(currentState))
    }
}
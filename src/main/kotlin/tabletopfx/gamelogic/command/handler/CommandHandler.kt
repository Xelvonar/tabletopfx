package tabletopfx.gamelogic.command.handler

import tabletopfx.gamelogic.command.GameCommand
import tabletopfx.gamelogic.event.GameEvent
import tabletopfx.gamelogic.state.GameState

/**
 * ### Architectural Role: Authoritative State Core Validator (CQRS Domain Brain)
 * Defines the stateless behavioral execution wrapper matched against specific client intents.
 * Handlers are completely deterministic; they ingest ambient truths out of the fresh [GameState] snapshot,
 * judge incoming [GameCommand] rules, and output verified, immutable historic event logs.
 *
 * @param T Concrete variation subtype of [GameCommand] managed by this specific rules processor.
 */
interface CommandHandler<T : GameCommand> {

    /**
     * Evaluates incoming request variables against game rules, bitmasks, or grid coordinates.
     * Must remain completely pure and free of side-effects.
     *
     * @param command Inbound request context bundle to test.
     * @param gameState Fresh, un-tamperable truth baseline snapshot out of the engine timeline.
     * @return True if the intent is completely legal, False if a rule is broken.
     */
    fun validate(command: T, gameState: GameState): Boolean

    /**
     * Processes verified variables to forge a permanent domain history fact record.
     *
     * @param command Inbound request context bundle to execute.
     * @param gameState Fresh truth baseline snapshot out of the engine timeline.
     * @return A concrete subclass instance of [GameEvent] tracking the success transaction.
     */
    fun execute(command: T, gameState: GameState): GameEvent
}
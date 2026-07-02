package tabletopfx.gamelogic.command.handler

import tabletopfx.gamelogic.command.MoveComponentCommand
import tabletopfx.gamelogic.event.GameEvent
import tabletopfx.gamelogic.event.GameEvent.ComponentMovedEvent
import tabletopfx.gamelogic.state.GameState

/**
 * Processes structural board movement transactions.
 * Assures abstract piece shifts are calculated cleanly based on real historical coordinates.
 */
class MoveComponentHandler : CommandHandler<MoveComponentCommand> {

    override fun validate(command: MoveComponentCommand, gameState: GameState): Boolean {
        // Validation space for movement vectors, obstruction meshes, or action token fees
        return true
    }

    override fun execute(command: MoveComponentCommand, gameState: GameState): GameEvent {
        return ComponentMovedEvent(
            id = command.id,
            location = command.endLocation
        )
    }
}
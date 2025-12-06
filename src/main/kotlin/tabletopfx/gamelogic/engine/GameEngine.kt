package tabletopfx.gamelogic.engine

import tabletopfx.gamelogic.GameState
import tabletopfx.gamelogic.command.GameCommand
import tabletopfx.gamelogic.command.MovePieceCommand
import tabletopfx.gamelogic.component.PieceComponent

class GameEngine(
    val state: GameState
) {
    private val history = ArrayDeque<GameCommand>()
    private val redoStack = ArrayDeque<GameCommand>()

    fun execute(command: GameCommand) {
        command.apply(state)
        history.addLast(command)
        redoStack.clear()

        notifyUiCommandApplied(command)
    }

    fun undo() {
        val command = history.removeLastOrNull() ?: return
        command.undo(state)
        redoStack.addLast(command)

        notifyUiCommandUndone(command)
    }

    fun redo() {
        val command = redoStack.removeLastOrNull() ?: return
        command.apply(state)
        history.addLast(command)

        notifyUiCommandApplied(command)
    }

    /**
     * Later this will use game logic to validate the move. Right now it assumes it is
     * valid and issues the move command.
     */
    fun requestPieceMove(piece: PieceComponent, x: Double, y: Double)  {
        execute(MovePieceCommand(piece.id, x, y))
    }

    fun notifyUiCommandApplied(com: GameCommand) {}

    fun notifyUiCommandUndone(com: GameCommand) {}
}
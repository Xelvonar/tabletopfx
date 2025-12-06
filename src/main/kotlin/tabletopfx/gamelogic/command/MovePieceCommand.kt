package tabletopfx.gamelogic.command

import tabletopfx.gamelogic.GameState
import tabletopfx.gamelogic.component.Position

// Move an existing piece from one location to another.
class MovePieceCommand(
    private val id: Int,
    private val toX: Double,
    private val toY: Double
) : GameCommand {

    private var fromX: Double? = null
    private var fromY: Double? = null

    override var description: String = ""

    override fun apply(state: GameState) {
        val piece = state.getPiece(id) ?: return
        fromX = piece.position.x
        fromY = piece.position.y
        piece.position = Position(toX, toY)
        description = "Move $piece.name from ($fromX, $fromY) to ($toX, $toY)"
    }

    override fun undo(state: GameState) {
        val piece = state.getPiece(id) ?: return
        val fx = fromX ?: return
        val fy = fromY ?: return

        piece.position = Position(fx, fy)
    }
}
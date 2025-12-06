package tabletopfx.gamelogic.command

import tabletopfx.gamelogic.GameState
import tabletopfx.gamelogic.component.PieceComponent
import tabletopfx.gamelogic.component.Position

// Create a new piece at a location.
class CreatePieceCommand(
    private val name: String,
    private val imageRef: String,
    private val x: Double,
    private val y: Double
) : GameCommand {

    private var createdPiece: PieceComponent? = null

    override val description: String = "Create $name at $x, $y"

    override fun apply(state: GameState) {
        // If first time, create the piece object
        if (createdPiece == null) {
            createdPiece = PieceComponent(name, imageRef, Position(x,y))
        }
        // Add to state
        createdPiece?.let { state.addPiece(it) }
    }

    override fun undo(state: GameState) {
        createdPiece?.let { state.removePiece(it.id) }
    }
}


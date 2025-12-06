package tabletopfx.gamelogic.command

import tabletopfx.gamelogic.GameState

interface GameCommand {
    val description: String //for logging

    fun apply(state: GameState)
    fun undo(state: GameState)
}
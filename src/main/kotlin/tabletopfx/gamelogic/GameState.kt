package tabletopfx.gamelogic

import tabletopfx.gamelogic.component.PieceComponent

class GameState {

    private val pieces: MutableMap<Int, PieceComponent> = mutableMapOf()

    fun addPiece(piece: PieceComponent) {
        pieces[piece.id] = piece
    }

    fun removePiece(id: Int): PieceComponent? =
        pieces.remove(id)

    fun getPiece(id: Int): PieceComponent? =
        pieces[id]

    fun allPieces(): Collection<PieceComponent> =
        pieces.values
}
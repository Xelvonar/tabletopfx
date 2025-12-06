package tabletopfx.gamelogic.property

sealed class PieceProperty {
    data class IntProp(val value: Int) : PieceProperty()
    data class DoubleProp(val value: Double) : PieceProperty()
    data class BoolProp(val value: Boolean) : PieceProperty()
    data class StringProp(val value: String) : PieceProperty()
}
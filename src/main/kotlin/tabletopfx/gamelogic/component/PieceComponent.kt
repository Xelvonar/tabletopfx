package tabletopfx.gamelogic.component

import tabletopfx.gamelogic.property.PieceProperty

class PieceComponent(
    name: String,
    imageRef: String,
    var position: Position

) : Component(name, imageRef) {

    val properties: MutableMap<String, PieceProperty> = mutableMapOf()

    // -------------------------
    // ONE SETTER. Use: pc.set("movement", 4). Detects type of property from the second parameter.
    // -------------------------
    fun set(name: String, value: Any) {
        val prop = when (value) {
            is Int -> PieceProperty.IntProp(value)
            is Double -> PieceProperty.DoubleProp(value)
            is Boolean -> PieceProperty.BoolProp(value)
            is String -> PieceProperty.StringProp(value)
            else -> throw IllegalArgumentException("Unsupported property type: ${value::class}")
        }
        properties[name] = prop
    }

    // -------------------------
    // ONE GETTER. Use: pc.get<Int>("movement")
    // -------------------------
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(name: String): T? {
        return when (val p = properties[name]) {
            is PieceProperty.IntProp    -> p.value as? T
            is PieceProperty.DoubleProp -> p.value as? T
            is PieceProperty.BoolProp   -> p.value as? T
            is PieceProperty.StringProp -> p.value as? T
            else -> null
        }
    }
}
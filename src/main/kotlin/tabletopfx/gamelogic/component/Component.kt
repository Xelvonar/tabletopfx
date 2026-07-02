package tabletopfx.gamelogic.component

import tabletopfx.gamelogic.property.BoolProp
import tabletopfx.gamelogic.property.DoubleProp
import tabletopfx.gamelogic.property.ImageProp
import tabletopfx.gamelogic.property.IntProp
import tabletopfx.gamelogic.property.StringProp
import java.util.UUID

/**
 * ### Architectural Role: Authoritative Entity Primitive Model
 * Serves as the primary data model class for all physical pieces, boards, decks, and elements
 * within the tabletop sandbox ecosystem. This entity is designed as a completely immutable data block
 * and handles state tracking entirely via pure functional copy transformation methods.
 *
 * Properties inside the target entity are kept layout-agnostic; all visual resource parameters (like graphic paths)
 * are encapsulated inside the dynamic [Aspect] metadata layer.
 *
 * @property id Clean unique immutable tracking key identifying this entity across engine logs and network vectors.
 * @property name The base identifying name of this component primitive used for log tracking outputs.
 * @property location Active spatial placement category indexing exactly where this element lives logic-wise.
 * @property aspects Complete localized lookup ledger mapping available aspect presentation variations.
 * @property currentAspectKey Active pointer mapping the specific sub-aspect context deployed on screen.
 * @see Location
 * @see Aspect
 */
data class Component(
    val id: UUID,
    val name: String,
    val location: Location,
    val aspects: Map<String, Aspect> = emptyMap(),
    val currentAspectKey: String = "default"
) {

    /**
     * Resolves the active [Aspect] data packet pointed to by the [currentAspectKey].
     * Throws an explicit initialization error if the map state lacks configuration keys.
     */
    val currentAspect: Aspect
        get() = aspects[currentAspectKey] ?: error("Unknown aspect '$currentAspectKey' for component '$name'")

    /**
     * Raw element data extractor method.
     * Safely probes the target properties ledger under a specified aspect key context to fetch property values.
     *
     * @param name Key lookup query string matching the required property name.
     * @param aspectKey Target aspect scope option to look inside. Defaults to the active [currentAspectKey].
     * @return The underlying unwrapped primitive type, or null if the key is unconfigured.
     */
    fun get(name: String, aspectKey: String = currentAspectKey): Any? {
        val targetAspect = aspects[aspectKey] ?: return null
        return when (val prop = targetAspect.properties[name]) {
            is IntProp -> prop.value
            is DoubleProp -> prop.value
            is BoolProp -> prop.value
            is StringProp -> prop.value
            is ImageProp -> prop.imageRef
            null          -> null
        }
    }

    // --- Type-Safe Fast Accessors ---
    fun getInt(name: String, aspectKey: String = currentAspectKey): Int? = get(name, aspectKey) as? Int
    fun getDouble(name: String, aspectKey: String = currentAspectKey): Double? = get(name, aspectKey) as? Double
    fun getBool(name: String, aspectKey: String = currentAspectKey): Boolean? = get(name, aspectKey) as? Boolean
    fun getString(name: String, aspectKey: String = currentAspectKey): String? = get(name, aspectKey) as? String

    /**
     * Functional Transformation: Evolves location variables.
     * Returns a fresh immutable clone containing the target [Location] update.
     */
    fun withLocation(newLocation: Location): Component {
        return this.copy(location = newLocation)
    }

    /**
     * Functional Transformation: Shifts the active aspect key layer.
     * Enforces explicit validation checks to guarantee the requested aspect is present inside the tracking maps.
     */
    fun withStateKey(newStateKey: String): Component {
        require(aspects.containsKey(newStateKey)) { "Aspect state '$newStateKey' does not exist on component '$name'" }
        return this.copy(currentAspectKey = newStateKey)
    }

    /**
     * Functional Transformation: Mutates a custom property metric cleanly.
     * Creates an updated primitive envelope, appends it to the matching target aspect tracking registry,
     * and outputs a completely pristine [Component] container leaf instance.
     *
     * @param name Mapping dictionary tracking key target.
     * @param value The value data element to mount inside the property index.
     * @param stateKey Target aspect layer container destination to edit. Defaults to [currentAspectKey].
     */
    fun withProperty(name: String, value: Any, stateKey: String = currentAspectKey): Component {
        val targetState = aspects[stateKey] ?: error("Aspect state '$stateKey' does not exist")

        val newProp = when (value) {
            is Int     -> IntProp(value)
            is Double  -> DoubleProp(value)
            is Boolean -> BoolProp(value)
            is String  -> StringProp(value)
            is ImageProp -> value
            else       -> throw IllegalArgumentException("Unsupported property payload mapping category: ${value::class}")
        }

        val updatedState = targetState.copy(
            properties = targetState.properties + (name to newProp)
        )

        return this.copy(aspects = this.aspects + (stateKey to updatedState))
    }
}
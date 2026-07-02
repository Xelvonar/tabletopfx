package tabletopfx.gamelogic.property

/**
 * ### Architectural Role: Type-Safe Logic Property Wrapper
 * Serves as the base super-interface representing any generic game attribute, custom statistic,
 * metadata flag, or visual asset hook bound to a component's aspect profile.
 * * By encapsulating raw types within this sealed container, the sandbox remains relational and flat,
 * permitting uniform property lookup mechanics while preventing unintended type intermixing.
 *
 * @see IntProp
 * @see DoubleProp
 * @see BoolProp
 * @see StringProp
 * @see ImageProp
 */
sealed interface Property

/**
 * Encapsulates standard integer metadata values.
 * Commonly stores attributes like health points, attack ratings, speed increments, or custom turn counters.
 *
 * @property value The underlying primitive [Int] payload state.
 */
data class IntProp(val value: Int) : Property

/**
 * Encapsulates high-precision decimal statistics.
 * Commonly tracks metrics requiring finer spatial or scale values, such as weight thresholds,
 * localized modifiers, or continuous point scales.
 *
 * @property value The underlying primitive [Double] payload state.
 */
data class DoubleProp(val value: Double) : Property

/**
 * Encapsulates binary logic status flags.
 * Used for states like visibility switches, tapped/untapped toggles, or general true/false rule gates.
 *
 * @property value The underlying primitive [Boolean] payload state.
 */
data class BoolProp(val value: Boolean) : Property

/**
 * Encapsulates general text parameter data strings.
 * Used for dynamic logic queries, item descriptions, lore text snippets, or system identifiers.
 *
 * @property value The underlying standard [String] payload state.
 */
data class StringProp(val value: String) : Property

/**
 * Encapsulates a visual asset texture link token.
 * This specific wrapper type isolates visual imagery pointers (such as file layout paths or image asset paths)
 * from direct rendering handles, ensuring the core engine maps art layouts layout-agnostically.
 *
 * @property imageRef Unique resource identifier pointing toward the graphical bundle file asset (e.g., "cyan-meeple.png").
 */
data class ImageProp(val imageRef: String) : Property
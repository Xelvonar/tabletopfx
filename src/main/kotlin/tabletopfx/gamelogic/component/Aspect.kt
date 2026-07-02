package tabletopfx.gamelogic.component

import tabletopfx.gamelogic.property.Property

/**
 * ### Architectural Role: Component State Snapshot Data Payload
 * Encapsulates an isolated thematic profile mapping out a distinct layout presentation or stat array.
 * This class isolates distinct dynamic runtime properties (like card fronts versus card backs, or
 * item upgrades) without leaking type maps across active logic transformations.
 *
 * @property name The human-readable identifying title of this specific aspect context (e.g., "front", "back").
 * @property properties The type-safe map ledger indexing all operational fields bound to this localized aspect state.
 * @see Component
 * @see Property
 */
data class Aspect(
    val name: String,
    val properties: Map<String, Property> = emptyMap()
)
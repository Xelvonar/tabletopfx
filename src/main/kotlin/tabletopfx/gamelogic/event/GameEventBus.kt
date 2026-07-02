package tabletopfx.gamelogic.event

import kotlin.reflect.KClass

/**
 * ### Architectural Role: Reified Publish-Subscribe Event Pipeline
 * Coordinates decouple data transmissions across framework boundaries using high-efficiency type routing keys.
 * This router acts as the primary synchronization conduit letting visual response handlers observe logic engine
 * events cleanly without hardcoding type-cast parameters.
 */
class GameEventBus {

    /**
     * Internal lookup registry tracking structural subscriptions keyed by type signatures.
     * Annotated with `@PublishedApi` to satisfy inline visibility constraints during call-site byte injection.
     */
    @PublishedApi
    internal val listeners = HashMap<KClass<out GameEvent>, MutableList<(GameEvent) -> Unit>>()

    /**
     * Synchronously delivers a domain fact payload to all registered listener scopes.
     *
     * @param event Concrete history instance containing contextual data parameters.
     */
    fun publish(event: GameEvent) {
        listeners[event::class]?.forEach { handler ->
            handler(event)
        }
    }

    /**
     * Mounts a type-safe consumer lambda onto the active data pipeline loop.
     * Leverages reified generics to cleanly deduce event categories without reflection boilerplate.
     *
     * ```kotlin
     * bus.subscribe<ComponentMovedEvent> { event ->
     * println("Component ${event.id} shifted quadrants safely.")
     * }
     * ```
     * Note to self: I don't really understand the inline reified, but it works.
     *
     * @param T Targeted subclass variant of [GameEvent] to trap.
     * @param handler Functional processing lambda code block invoked when a matching message clears.
     */
    inline fun <reified T : GameEvent> subscribe(noinline handler: (T) -> Unit) {
        val eventType = T::class
        val list = listeners.getOrPut(eventType) { mutableListOf() }

        @Suppress("UNCHECKED_CAST")
        list.add(handler as (GameEvent) -> Unit)
    }
}
package tabletopfx.gamelogic.command.handler

import tabletopfx.gamelogic.command.CreateComponentCommand
import tabletopfx.gamelogic.component.Aspect
import tabletopfx.gamelogic.component.Component
import java.util.UUID
import tabletopfx.gamelogic.event.GameEvent
import tabletopfx.gamelogic.event.GameEvent.ComponentCreatedEvent
import tabletopfx.gamelogic.property.ImageProp
import tabletopfx.gamelogic.state.GameState

/**
 * Processes rules surrounding new component initialization milestones.
 * Resolves safe domain identifier values to isolate views away from system generation tasks.
 */
class CreateComponentHandler : CommandHandler<CreateComponentCommand> {

    override fun validate(command: CreateComponentCommand, gameState: GameState): Boolean {
        // Validation space for spawning caps, maximum piece ceilings, or turn restrictions
        return true
    }

    override fun execute(command: CreateComponentCommand, gameState: GameState): GameEvent {
        val initialProperties = if (command.imageRef != null) {
            mapOf("image" to ImageProp(command.imageRef))
        } else {
            emptyMap()
        }

        val initialAspect = Aspect(
            name = command.initialAspectName,
            properties = initialProperties
        )

        val newComponent = Component(
            id = UUID.randomUUID(),
            name = command.name,
            location = command.loc,
            aspects = mapOf(command.initialAspectName to initialAspect),
            currentAspectKey = command.initialAspectName
        )
        return ComponentCreatedEvent(newComponent)
    }
}
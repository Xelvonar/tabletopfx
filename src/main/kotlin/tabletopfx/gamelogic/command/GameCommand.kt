package tabletopfx.gamelogic.command

import tabletopfx.gamelogic.component.Location
import java.util.UUID

/**
 * ### Architectural Role: Transaction Intent Envelope
 * Establishes the universal supertype contract for atomic transactional requests dispatched
 * down to the core logic engine loop. Commands house minimum, raw payload values describing user intent.
 *
 * @property commandId Universally unique tracing key attached to this transaction frame for log audit paths.
 * @see tabletopfx.gamelogic.command.handler.CommandHandler
 */
interface GameCommand {
    val commandId: UUID
}

/**
 * Transaction payload encapsulating the intent to instantiate a new component piece.
 *
 * @property name Human-readable string identifier assigned to the piece.
 * @property loc Target logical layout destination cell where the piece is born.
 * @property initialAspectName Identifying dictionary key mapping out which visual style is applied first.
 * @property imageRef Raw texture graphic bundle resource target file (e.g. "cyan-meeple.png").
 */
data class CreateComponentCommand(
    val name: String,
    val loc: Location,
    val initialAspectName: String = "default",
    val imageRef: String? = null,
    override val commandId: UUID = UUID.randomUUID()
) : GameCommand

/**
 * Transaction payload encapsulating the intent to translate a component across grid cells.
 * Contains only the target's relational pointer and intended landing destination.
 *
 * @property id The authoritative [UUID] matching the core domain component in motion.
 * @property endLocation Terminal target logical layout placement coordinates requested.
 */
data class MoveComponentCommand(
    val id: UUID,
    val endLocation: Location,
    override val commandId: UUID = UUID.randomUUID()
) : GameCommand
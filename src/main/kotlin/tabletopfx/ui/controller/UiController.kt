package tabletopfx.ui.controller

import java.util.UUID
import tabletopfx.gamelogic.command.GameCommand
import tabletopfx.gamelogic.engine.GameEngine
import tabletopfx.gamelogic.event.GameEventBus
import tabletopfx.ui.location.LocationTranslator
import tabletopfx.ui.mode.IdleMode
import tabletopfx.ui.mode.UiMode
import tabletopfx.ui.renderer.ComponentRenderer
import tabletopfx.ui.selection.SelectionModel
import tabletopfx.ui.view.BoardView

/**
 * ### Architectural Role: Unified UI Orchestration Brain (The Intersector)
 * Serves as the authoritative architectural bridge separating the "dumb" layout canvas framework
 * ([BoardView]) from the pure rules environment ([GameEngine]).
 * It coordinates the central Finite State Machine, parsing raw interactive mouse or keyboard vectors
 * out of window panes and delegating them to the active [UiMode]. This ensures that visual layouts remain completely
 * rule-blind, and business handlers remain entirely pixel-agnostic.
 *
 * @property view The concrete composite layout node tree stack managing graphics sub-layers.
 * @property engine The authoritative transaction pipeline processing engine loop.
 * @property selectionModel Long-lived, client-side focal state tracking set container.
 * @property eventBus Type-safe infrastructure message bus highway.
 * @property locationTranslator Spatial translation strategy mapping visual pixel doubles back to logical domain indicators.
 * @see UiMode
 * @see GameEngine
 * @see BoardView
 */
class UiController(
    val view: BoardView,
    private val engine: GameEngine,
    private val selectionModel: SelectionModel,
    private val eventBus: GameEventBus,
    val locationTranslator: LocationTranslator
) {

    /**
     * Active state machine interaction context block.
     * Enforces restricted write authorization fields to guarantee strict lifecycle transformations.
     */
    var activeMode: UiMode = IdleMode
        private set

    init {
        wireViewInputHandlers()
        setupEngineEventSubscriptions()

        activeMode.onEnter(this)
    }

    /**
     * Maps physical hardware layer gestures directly onto the active state matrix execution blocks.
     */
    private fun wireViewInputHandlers() {
        val componentLayer = view.getComponentLayer()

        // --- Component Interaction Conduit Connections ---
        componentLayer.onComponentClicked = { id, event -> activeMode.onComponentClicked(this, id, event) }
        componentLayer.onComponentDragStarted = { id, event -> activeMode.onComponentDragStarted(this, id, event) }
        componentLayer.onComponentDragContinued = { id, event -> activeMode.onComponentDragged(this, id, event) }
        componentLayer.onComponentDragReleased = { id, event -> activeMode.onComponentReleased(this, id, event) }

        // --- Canvas Background Interaction Conduit Connections ---
        view.onBoardPressed = { point, event -> activeMode.onBoardPressed(this, point, event) }
        view.onBoardDragged = { point, event -> activeMode.onBoardDragged(this, point, event) }
        view.onBoardReleased = { point, event -> activeMode.onBoardReleased(this, point, event) }

        // --- Keyboard Focus Hook Observer ---
        view.sceneProperty().addListener { _, _, newScene ->
            newScene?.setOnKeyPressed { event -> activeMode.onKeyPressed(this, event) }
        }
    }

    /**
     * Drives a type-safe context swap across the Finite State Machine timeline.
     * Executes strict, self-cleaning hook procedures to transition active selections safely.
     *
     * @param newMode Upcoming targeted class instance to mount onto the input driver.
     */
    fun switchToMode(newMode: UiMode) {
        if (activeMode != newMode) {
            activeMode.onExit(this)
            activeMode = newMode
            activeMode.onEnter(this)
        }
    }

    /**
     * Forwards a clean client intent block down into the logic engine's command processing queue.
     *
     * @param cmd Target payload envelope containing validation details.
     */
    fun requestCommand(cmd: GameCommand) {
        engine.request(cmd)
    }

    /**
     * Mounts a targeted element identifier context directly onto the client selection ledger.
     *
     * @param id The authoritative [UUID] representing the clicked piece.
     */
    fun selectComponent(id: UUID) {
        selectionModel.select(id)
    }

    /**
     * Flushes tracked token focus parameters cleanly out of the active client dictionary.
     */
    fun clearSelection() {
        selectionModel.clear()
    }

    /**
     * Instantiates and registers the long-lived, read-path renderers onto the event bus.
     */
    private fun setupEngineEventSubscriptions() {
        ComponentRenderer().register(eventBus, view, this)
    }
}
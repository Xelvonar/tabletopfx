package tabletopfx

import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage
import tabletopfx.gamelogic.command.CreateComponentCommand
import tabletopfx.gamelogic.engine.GameEngine
import tabletopfx.gamelogic.state.GameState
import tabletopfx.gamelogic.event.GameEvent.MessageLoggedEvent
import tabletopfx.gamelogic.event.GameEventBus
import tabletopfx.gamelogic.command.GameCommand
import tabletopfx.gamelogic.command.MoveComponentCommand
import tabletopfx.gamelogic.command.handler.CommandHandler
import tabletopfx.gamelogic.command.handler.CreateComponentHandler
import tabletopfx.gamelogic.command.handler.MoveComponentHandler
import tabletopfx.gamelogic.component.SquareGridLoc
import tabletopfx.gamelogic.event.GameEvent.ComponentCreatedEvent
import tabletopfx.ui.view.BoardView
import tabletopfx.ui.selection.SelectionModel
import tabletopfx.ui.location.SquareGridTranslator
import tabletopfx.ui.renderer.ComponentRenderer
import tabletopfx.ui.controller.UiController
import java.util.UUID
import kotlin.reflect.KClass

class Main : Application() {
    override fun start(stage: Stage) {
        // 1. Initialize Event Infrastructure
        val eventBus = GameEventBus()

        // Permanent system log console printer
        eventBus.subscribe<MessageLoggedEvent> { event ->
            println("[GAME LOG] ${event.text}")
        }

        // 2. FORGE THE COMMAND REGISTRY (Aligned with GameEngine Java Class lookup contract)
        val registry: Map<KClass<out GameCommand>, CommandHandler<*>> = mapOf(
            MoveComponentCommand::class to MoveComponentHandler(),
            CreateComponentCommand::class to CreateComponentHandler()
        )


        // 3. Initialize Authoritative Logic Loop
        val gameState = GameState()
        val gameEngine = GameEngine(gameState, eventBus, registry)

        // 4. Initialize Core View Architecture & Layer Stack
        val boardView = BoardView()

        // Load asset paths onto our background view pane safely
        boardView.setBackgroundImage("/images/Chess_board.png")

        // 5. Instantiate State Infrastructure & Strategy Converters
        val selectionModel = SelectionModel()

        // Calibrate these numbers to fit your true Chess_board.png cell size grid lines
        val spatialStrategy = SquareGridTranslator(
            originX = 5.0,
            originY = 5.0,
            cellWidth = 815.0/8,
            cellHeight = 815.0/8,
            maxRows = 8,
            maxCols = 8
        )

        // 6. Connect the Input Orchestrator brain
        val uiController = UiController(
            view = boardView,
            engine = gameEngine,
            selectionModel = selectionModel,
            eventBus = eventBus,
            locationTranslator = spatialStrategy
        )

        // 7. Core Unidirectional View Updates (Read Path Visualizers)
        val componentRenderer = ComponentRenderer()
        componentRenderer.register(eventBus, boardView, uiController)

        // 8. Mount Scene Tree Window Boilerplate
        stage.scene = Scene(boardView, 800.0, 800.0)
        stage.title = "TableTopFX Virtual Canvas Engine"
        val debugPieceId = UUID.randomUUID()
        val createCmd =
            CreateComponentCommand(
                name = "Letter A",
                loc = SquareGridLoc(0, 0),
                imageRef = "Square-a.png",
            )
        gameEngine.request(createCmd)
        stage.show()
    }
}

// Top-level entry point for the JVM
fun main(args: Array<String>) {
    Application.launch(Main::class.java, *args)
}
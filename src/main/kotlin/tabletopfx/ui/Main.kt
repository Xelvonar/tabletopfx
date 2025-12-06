package tabletopfx.ui

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage
import tabletopfx.gamelogic.GameState
import tabletopfx.gamelogic.command.CreatePieceCommand
import tabletopfx.gamelogic.command.MovePieceCommand
import tabletopfx.gamelogic.component.PieceComponent
import tabletopfx.gamelogic.engine.GameEngine
import tabletopfx.ui.MainView
import tabletopfx.ui.controller.UiController

class Main : Application() {

    private lateinit var gameState : GameState
    private lateinit var gameEngine : GameEngine
    private lateinit var mainView : MainView
    private lateinit var uiController: UiController

    override fun start(stage: Stage) {

        gameState = GameState()
        gameEngine = GameEngine(gameState)
        mainView = MainView()
        uiController = UiController(mainView, gameEngine, gameState)
//        root.boardView.backgroundLayer.setBackgroundLayerImage(
//            Image(javaClass.getResourceAsStream("/images/Normandy Map.jpg"))
//        )
        teststuff()
        val scene = Scene(mainView, 1500.0, 1500.0)
        stage.scene = scene
        stage.title = "TabletopFX"
        stage.show()
    }

    // You will implement this
    fun teststuff() {
        gameEngine.execute(CreatePieceCommand("meeple", "cyan-meeple.png",10.0,10.0))
        gameEngine.execute(CreatePieceCommand("meeple", "cyan-meeple.png",50.0,50.0))
        mainView.boardView.pieceLayer.addPiece(gameState.getPiece(0))
        mainView.boardView.pieceLayer.addPiece(gameState.getPiece(1))
//        gameEngine.execute(MovePieceCommand(0,50.0,50.0))
//        root.boardView.pieceLayer.removePiece(gameState.getPiece(0))
//        root.boardView.pieceLayer.addPiece(gameState.getPiece(0))
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(Main::class.java, *args)
        }
    }
}
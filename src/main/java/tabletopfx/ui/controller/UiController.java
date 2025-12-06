package tabletopfx.ui.controller;

import java.util.Objects;

import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import tabletopfx.gamelogic.GameState;
import tabletopfx.gamelogic.component.PieceComponent;
import tabletopfx.gamelogic.engine.GameEngine;
import tabletopfx.ui.BoardView;
import tabletopfx.ui.MainView;
import tabletopfx.ui.layer.BackgroundLayer;
import tabletopfx.ui.layer.GridLayer;
import tabletopfx.ui.layer.PieceLayer;
import tabletopfx.ui.layer.UiLayer;



/**
 * Central UI controller that wires mouse events from the {@link BoardView}
 * into a pluggable {@link UiMode} state machine.
 *
 * <p>The controller:
 * <ul>
 *     <li>Owns a reference to {@link MainView}, and through it to the {@link BoardView}
 *         and its layers ({@link BackgroundLayer}, {@link PieceLayer},
 *         {@link GridLayer}, {@link UiLayer}).</li>
 *     <li>Installs JavaFX mouse handlers exactly once on these layers.</li>
 *     <li>Delegates all incoming events to the currently active {@link UiMode}.</li>
 *     <li>Maintains a small set of mode instances (e.g. {@link WaitingMode},
 *         {@link PieceClickedMode}) and switches between them via {@link #setMode(UiMode)}.</li>
 * </ul>
 *
 * <p>By default, all mode methods are no-ops (via default methods in {@link UiMode}),
 * so unhandled events are simply ignored.</p>
 */
public class UiController {

    private final MainView mainView;
    private final GameEngine gameEngine;
    private final GameState gameState;

    /**
     * The currently active UI mode. All incoming mouse events are delegated here.
     */
    private UiMode mode;

    /**
     * Pre-instantiated modes (constructed once when the controller is created).
     * They can be reused repeatedly rather than allocating new objects on each transition.
     */
    private final UiMode waitingMode;
    private final UiMode pieceClickedMode;


    /**
     * Creates a new {@code UiController} and wires mouse event handlers from
     * the {@link BoardView} into the mode dispatch system.
     *
     * <p>The initial mode is {@link WaitingMode}.</p>
     *
     * @param mainView the root view that owns the {@link BoardView} and its layers
     */
    public UiController(MainView mainView, GameEngine gameEngine, GameState gameState) {
        this.mainView = mainView;
        this.gameEngine = gameEngine;
        this.gameState = gameState;

        // Construct modes once and reuse them.
        this.waitingMode = new WaitingMode();
        this.pieceClickedMode = new PieceClickedMode();

        // Initial mode
        this.mode = waitingMode;

        wireHandlers();
    }

    /**
     * Installs JavaFX mouse event handlers on the {@link BoardView} layers.
     *
     * <p>The handlers are wired exactly once. They simply delegate each event
     * to the current {@link UiMode}. Because {@link UiMode} supplies default
     * no-op implementations, any event not explicitly handled by a mode is ignored.</p>
     */
    private void wireHandlers() {
        BoardView boardView = mainView.getBoardView();

        BackgroundLayer background = boardView.getBackgroundLayer();
        PieceLayer pieces = boardView.getPieceLayer();
        GridLayer grid = boardView.getGridLayer();
        UiLayer uiLayer = boardView.getUiLayer();

        // Background: clicks and moves on “bare board”
        background.setLayerClickHandler(
                event -> mode.onBackgroundClick(this, event)
        );
        background.setLayerMoveHandler(
                event -> mode.onBackgroundMove(this, event)
        );

        // Piece layer: per-piece clicks (piece node consumes the event)
        pieces.setPieceClickHandler(
                (piece, event) -> mode.onPieceClick(this, piece, event)
        );

        // Optional: layer-wide click in piece layer (empty area between pieces)
        pieces.setLayerClickHandler(
                event -> mode.onPieceLayerClick(this, event)
        );

        // Grid layer: snap points, zones, etc.
        grid.setLayerClickHandler(
                event -> mode.onGridClick(this, event)
        );
        grid.setLayerMoveHandler(
                event -> mode.onGridMove(this, event)
        );

        // UI overlay: drag rectangles, hover highlights, etc.
        uiLayer.setLayerMoveHandler(
                event -> mode.onUiLayerMove(this, event)
        );
        uiLayer.setLayerClickHandler(
                event -> mode.onUiLayerClick(this, event)
        );
    }

    /**
     * Sets the current {@link UiMode}. Subsequent events will be dispatched
     * to the new mode's handler methods.
     *
     * @param newMode the mode to activate; must not be {@code null}
     */
    public void setMode(UiMode newMode) {
        if (newMode == null) {
            throw new IllegalArgumentException("UiMode must not be null");
        }
        this.mode = newMode;
    }

    /**
     * Convenience method to switch back to the "waiting" mode.
     */
    public void switchToWaitingMode() {
        setMode(waitingMode);
    }

    // -------------------------------------------------------------------------
    // PieceSelectedMode methods and variables.
    // -------------------------------------------------------------------------

    /**
     * Holds the pieceComponent (game logic representation) of the selected piece.
     */
    private PieceComponent selectedPiece;

    /**
     * Holds the node in PieceLayer of the selected piece.
     */
    private ImageView selectedPieceNode ;

    /**
     * Holds the image of the selection in UiLayer in PieceSelectedMode
     */
    private ImageView selectionImage;

    /**
     * Convenience method to switch into the "piece clicked" mode.
     */
    public void switchToPieceClickedMode() {
        setMode(pieceClickedMode);
    }

    /**
     * Clears any currently selected piece and removes associated selection image in UiLayer.
     * Restores visibility and MouseListening of its node in PieceLayer
     */
    public void clearSelection() {
        selectedPiece = null;

        if (selectedPieceNode != null) {
            selectedPieceNode.setVisible(true);
            selectedPieceNode.setMouseTransparent(false);
            selectedPieceNode = null;
        }

        if (selectionImage != null) {
            mainView.getBoardView().getUiLayer().getChildren().remove(selectionImage);
            selectionImage = null;
        }
    }

    /**
     * Marks the given piece as selected and updates any visual indication
     * (e.g. highlight rectangle in {@link UiLayer}).
     *
     * @param piece the piece to select
     */
    public void selectPiece(PieceComponent piece, MouseEvent event) {
        this.selectedPiece = piece;
        this.selectedPieceNode = mainView.getBoardView().getPieceLayer().getPieceNodes().get(piece);

        // Remove any existing selection image
        if (selectionImage != null) {
            mainView.getBoardView().getUiLayer()
                    .getChildren().remove(selectionImage);
            selectionImage = null;
        }

        // Load the same image used by the piece (imageRef should be a classpath resource, e.g. "/images/cyan-meeple.png")
        Image pieceImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(piece.getImageRef())));

        // Create an ImageView for the highlight
        this.selectionImage = new ImageView(pieceImage);
        selectionImage.setMouseTransparent(true); // don't steal mouse events from the board

        // Create a DropShadow that follows the PNG alpha (so irregular shapes like meeples work)
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(20);
        dropShadow.setSpread(0.6);
        dropShadow.setOffsetX(0);
        dropShadow.setOffsetY(0);
        dropShadow.setColor(Color.YELLOW);
        selectionImage.setEffect(dropShadow);

        // Position: piece's logical position + click offset
        double x = piece.getPosition().getX() + event.getX();
        double y = piece.getPosition().getY() + event.getY();

        selectionImage.setLayoutX(x);
        selectionImage.setLayoutY(y);

        // Make the image of the piece in the PieceLayer invisible and ignore MouseEvents
        selectedPieceNode.setVisible(false);
        selectedPieceNode.setMouseTransparent(true);

        // Add image of highlighted piece to UiLayer
        mainView.getBoardView().getUiLayer().getChildren().add(selectionImage);

    }
    /**
     * Updates the on-screen position of the temporary selection image used while
     * previewing a piece movement. This method repositions the image within the
     * {@code UiLayer} by setting its layout coordinates.
     *
     * <p>The selection image must already be attached to the scene graph.
     * This method does not perform bounds checking and does not validate
     * whether the coordinates lie within the board.</p>
     *
     * @param x the target x-coordinate in the {@code UiLayer}'s local coordinate space
     * @param y the target y-coordinate in the {@code UiLayer}'s local coordinate space
     */
    public void previewMoveAt(double x, double y){
        selectionImage.setLayoutX(x);
        selectionImage.setLayoutY(y);
    }

    /**
     * @return the currently selected piece, or {@code null} if none
     */
    public PieceComponent getSelectedPiece() {
        return selectedPiece;
    }

    /**
     * Moves the currently selected piece to the given board coordinates.
     * In a real implementation, this should likely dispatch a GameCommand
     * to the game engine, then update the UI accordingly.
     *
     * @param x target x-coordinate in board space
     * @param y target y-coordinate in board space
     */
    public void moveSelectedPieceTo(double x, double y) {
        if (selectedPiece == null) {
                        return;
        }
        gameEngine.requestPieceMove(PieceComponent selectedPiece, double x, double y);
        }
        // TODO: issue MovePieceCommand, refresh PieceLayer positions, etc.
    }


}

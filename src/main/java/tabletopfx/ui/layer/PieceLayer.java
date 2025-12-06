package tabletopfx.ui.layer;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import tabletopfx.gamelogic.component.PieceComponent;

import java.util.HashMap;
import java.util.Map;

/**
 * JavaFX layer responsible for displaying {@link PieceComponent} instances
 * as {@link ImageView} nodes on top of the board.
 *
 * <p>This class extends {@link BoardViewLayer}, inheriting:
 * <ul>
 *     <li>A base {@code ImageView} (which {@code PieceLayer} does not use by default).</li>
 *     <li>Layer-wide mouse callbacks via
 *         {@link BoardViewLayer#setLayerClickHandler(BoardViewLayer.ClickHandler)}
 *         and
 *         {@link BoardViewLayer#setLayerMoveHandler(BoardViewLayer.MoveHandler)}.</li>
 * </ul>
 *
 * <p>On top of that, {@code PieceLayer} manages a bidirectional association between
 * domain-level {@link PieceComponent} objects and their on-screen {@link ImageView}
 * representations, and supports per-piece mouse click handling via
 * {@link PieceClickHandler}.</p>
 */
public class PieceLayer extends BoardViewLayer {

    /**
     * Mapping from piece components (game logic) to ImageView nodes (UI).
     * <p>
     * Each entry represents a single visual instance of a {@link PieceComponent}
     * in this layer. The map is maintained by {@link #addPiece(PieceComponent)}
     * and {@link #removePiece(PieceComponent)}.
     */
    private final Map<PieceComponent, ImageView> pieceNodes = new HashMap<>();

    /**
     * Returns the internal mapping of piece components to their {@link ImageView} nodes.
     * <p>
     * The returned map is the live backing map. It should generally be treated as
     * read-only from outside this class; mutating it directly can desynchronize the
     * mapping from the actual JavaFX scene graph.
     *
     * @return the map from {@link PieceComponent} to {@link ImageView}
     */
    public Map<PieceComponent, ImageView> getPieceNodes() {
        return pieceNodes;
    }

    /**
     * Adds a new piece to this layer.
     *
     * <p>This method:
     * <ol>
     *     <li>Creates an {@link ImageView} for the given piece via
     *         {@link #createNodeForPiece(PieceComponent)}.</li>
     *     <li>Associates the piece with the node using {@link ImageView#setUserData(Object)}.</li>
     *     <li>Installs a per-piece mouse click handler that delegates to the
     *         configured {@link PieceClickHandler}, if present.</li>
     *     <li>Adds the node to this layer's children.</li>
     *     <li>Stores the mapping in {@link #pieceNodes}.</li>
     * </ol>
     *
     * @param pc the piece to add; must not be {@code null}
     */
    public void addPiece(PieceComponent pc) {
        ImageView node = createNodeForPiece(pc);
        node.setUserData(pc);

        // Per-piece click handler; note that the event is consumed
        // so the layer-level click handler will not see clicks on pieces.
        node.setOnMouseClicked(event -> {
            if (clickHandler != null) {
                clickHandler.onPieceClicked(pc, event);
            }
            event.consume();
        });

        getChildren().add(node);
        pieceNodes.put(pc, node);
    }

    /**
     * Creates and configures an {@link ImageView} for the given piece component.
     *
     * <p>By default, this implementation:
     * <ul>
     *     <li>Loads the image referenced by {@link PieceComponent#getImageRef()}
     *         from {@code /images/} on the classpath.</li>
     *     <li>Positions the view at the piece's logical {@code (x, y)} coordinates
     *         via {@link ImageView#setLayoutX(double)} and
     *         {@link ImageView#setLayoutY(double)}.</li>
     * </ul>
     *
     * <p>Subclasses may override this method if they need custom node creation,
     * scaling, or additional visual effects (e.g., drop shadows, selection outlines).</p>
     *
     * @param pc the piece to visualize
     * @return a newly created {@link ImageView} associated with the piece
     */
    protected ImageView createNodeForPiece(PieceComponent pc) {
        Image img = new Image(
                getClass().getResourceAsStream("/images/" + pc.getImageRef())
        );

        ImageView view = new ImageView(img);

        // Position according to logical state
        view.setLayoutX(pc.getPosition().getX());
        view.setLayoutY(pc.getPosition().getY());

        return view;
    }

    /**
     * Removes a piece and its visual representation from this layer, if present.
     *
     * <p>If the specified piece is currently mapped to an {@link ImageView}, this
     * method:
     * <ul>
     *     <li>Removes the node from the JavaFX scene graph.</li>
     *     <li>Removes the corresponding entry from {@link #pieceNodes}.</li>
     * </ul>
     * If the piece is not present, this method does nothing.</p>
     *
     * @param pc the piece to remove; may be {@code null} (in which case nothing happens)
     */
    public void removePiece(PieceComponent pc) {
        if (pc == null) {
            return;
        }
        ImageView node = pieceNodes.remove(pc);
        if (node != null) {
            getChildren().remove(node);
        }
    }

    /**
     * Clears all visible piece nodes from this layer.
     *
     * <p>This method removes only the {@link ImageView} nodes that are associated
     * with pieces in {@link #pieceNodes} from the scene graph. The backing map
     * itself is <strong>not</strong> cleared, preserving the original behavior
     * where the map may still contain references to {@link ImageView} instances
     * that are no longer attached to the scene graph.</p>
     *
     * <p>Note: If you want to completely reset this layer, including the mapping,
     * you can additionally call {@code getPieceNodes().clear()} from the caller.</p>
     */
    public void clearPieces() {
        for (ImageView node : pieceNodes.values()) {
            getChildren().remove(node);
        }
        // Intentionally do not clear pieceNodes, to match original semantics.
    }

    /**
     * Functional interface for receiving click events on individual pieces.
     *
     * <p>Unlike the layer-wide click handler exposed by {@link BoardViewLayer},
     * a {@code PieceClickHandler} is invoked specifically when a piece's
     * {@link ImageView} is clicked. The {@link MouseEvent} is consumed after
     * this callback, preventing it from bubbling to the layer-level handler.</p>
     */
    @FunctionalInterface
    public interface PieceClickHandler {

        /**
         * Called when a piece's {@link ImageView} is clicked.
         *
         * @param piece the domain model piece that was clicked
         * @param event the JavaFX mouse event associated with the click
         */
        void onPieceClicked(PieceComponent piece, MouseEvent event);
    }

    /**
     * Currently configured per-piece click handler, or {@code null} if none.
     */
    private PieceClickHandler clickHandler;

    /**
     * Sets the callback that fires whenever a piece {@link ImageView} is clicked.
     *
     * <p>To disable per-piece click handling, pass {@code null}.</p>
     *
     * @param handler the click handler, or {@code null} to disable callbacks
     */
    public void setPieceClickHandler(PieceClickHandler handler) {
        this.clickHandler = handler;
    }

    /**
     * Constructs a new {@code PieceLayer}.
     *
     * <p>The constructor:
     * <ul>
     *     <li>Invokes {@link BoardViewLayer#BoardViewLayer()} to initialize
     *         the base layer functionality.</li>
     *     <li>Adds the CSS style class {@code "piece-layer"} so that
     *         piece-specific styling can be applied via stylesheets.</li>
     * </ul>
     *
     * <p>By default, no pieces and no {@link PieceClickHandler} are installed.
     * Client code is expected to populate the layer via {@link #addPiece(PieceComponent)}
     * and configure callbacks via {@link #setPieceClickHandler(PieceClickHandler)}.</p>
     */
    public PieceLayer() {
        super();
        getStyleClass().add("piece-layer");
    }
}

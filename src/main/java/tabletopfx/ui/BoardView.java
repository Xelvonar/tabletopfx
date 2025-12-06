package tabletopfx.ui;


import tabletopfx.ui.layer.BackgroundLayer;
import tabletopfx.ui.layer.GridLayer;
import tabletopfx.ui.layer.PieceLayer;
import tabletopfx.ui.layer.UiLayer;
import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;

/**
 * BoardView is the central board area.
 * It stacks multiple visual layers:
 *  Bottom layer is BackgroundLayer: board image/hexes/terrain
 *  Next is GridLayer: snap points and zones
 *  Next is PieceLayer: game pieces
 *  Top Layer is UiLayer: selection boxes, highlights, drag overlays
 */
public class BoardView extends StackPane {

    private final BackgroundLayer backgroundLayer;
    private final GridLayer gridLayer;
    private final PieceLayer pieceLayer;
    private final UiLayer uiLayer;

    public BoardView() {
        this.backgroundLayer = new BackgroundLayer();
        this.gridLayer = new GridLayer();
        this.pieceLayer = new PieceLayer();
        this.uiLayer = new UiLayer();

        configureLayout();
        buildLayerStack();
    }

    private void configureLayout() {
        setPadding(new Insets(8));
        getStyleClass().add("board-view");

        // Optional: size hints; Scene/parent will usually override.
        setPrefSize(800, 800);
    }

    private void buildLayerStack() {
        // Order matters: first added = back, last = front
        getChildren().addAll(
                backgroundLayer,
                gridLayer,
                pieceLayer,
                uiLayer
        );
    }

    public BackgroundLayer getBackgroundLayer() {
        return backgroundLayer;
    }

    public GridLayer getGridLayer() {
        return gridLayer;
    }

    public PieceLayer getPieceLayer() {
        return pieceLayer;
    }

    public UiLayer getUiLayer() {
        return uiLayer;
    }
}

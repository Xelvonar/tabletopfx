package tabletopfx.ui.layer;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 * Base class for all visual layers in a {@code BoardView}.
 * <p>
 * A {@code BoardViewLayer} is a {@link Pane} that:
 * <ul>
 *     <li>Provides an optional {@link ImageView} that can display a layer-wide image
 *         (for example, a static background graphic).</li>
 *     <li>Exposes simple callback interfaces for mouse click and mouse move events
 *         occurring anywhere on the layer.</li>
 *     <li>Applies a common CSS style class {@code "boardview-layer"} so that
 *         all layers can share baseline styling.</li>
 * </ul>
 * Subclasses (such as {@code BackgroundLayer}, {@code GridLayer},
 * {@code PieceLayer}, and {@code UiLayer}) can build additional behavior
 * on top of this foundation.
 */
public abstract class BoardViewLayer extends Pane {

    /**
     * Internal image view used to display an optional layer-wide image.
     * <p>
     * Subclasses that do not need a single full-layer image may simply ignore it.
     */
    private final ImageView imageView = new ImageView();

    /**
     * Assigns an image to be displayed in this layer.
     * <p>
     * By default, the internal {@link ImageView} is added as the bottom-most child
     * of the layer and its {@code fitWidth} / {@code fitHeight} are bound to the
     * layer's width and height so that the image fills the drawable area.
     *
     * @param img the image to display, or {@code null} to clear the current image
     */
    public void setLayerImage(Image img) {
        imageView.setImage(img);
    }

    /**
     * Removes any image currently displayed by this layer's internal {@link ImageView}.
     * <p>
     * This is equivalent to calling {@code setLayerImage(null)}.
     */
    public void clearLayerImage() {
        imageView.setImage(null);
    }

    /**
     * Receives mouse click events that occur anywhere on the layer.
     * <p>
     * Implementations are typically provided as lambdas:
     * <pre>{@code
     * layer.setLayerClickHandler(event -> {
     *     System.out.println("Clicked at: " + event.getX() + ", " + event.getY());
     * });
     * }</pre>
     * Coordinates ({@link MouseEvent#getX()} and {@link MouseEvent#getY()})
     * are expressed in the layer's local coordinate space.
     */
    @FunctionalInterface
    public interface ClickHandler {

        /**
         * Called when a mouse click occurs on the layer.
         *
         * @param event the mouse event associated with the click
         */
        void onLayerMouseClicked(MouseEvent event);
    }

    /**
     * Receives mouse move events occurring over the layer.
     * <p>
     * This is useful for hover effects, previews, or other contextual feedback
     * that depends on the current mouse position.
     * Coordinates ({@link MouseEvent#getX()} and {@link MouseEvent#getY()})
     * are expressed in the layer's local coordinate space.
     */
    @FunctionalInterface
    public interface MoveHandler {

        /**
         * Called when the mouse moves over the layer.
         *
         * @param event the mouse event associated with the movement
         */
        void onLayerMouseMoved(MouseEvent event);
    }

    private ClickHandler clickHandler;
    private MoveHandler moveHandler;

    /**
     * Sets the callback invoked when the user clicks on this layer.
     * <p>
     * The handler receives all mouse click events that bubble up to the layer,
     * including clicks on child nodes contained within it.
     *
     * @param handler a click handler implementation, or {@code null} to disable layer-level click handling
     */
    public void setLayerClickHandler(ClickHandler handler) {
        this.clickHandler = handler;
    }

    /**
     * Sets the callback invoked when the mouse moves over this layer.
     * <p>
     * The handler receives all mouse move events that bubble up to the layer,
     * including movement over child nodes.
     *
     * @param handler a move handler implementation, or {@code null} to disable layer-level move handling
     */
    public void setLayerMoveHandler(MoveHandler handler) {
        this.moveHandler = handler;
    }

    /**
     * Constructs a new {@code BoardViewLayer}.
     * <p>
     * The constructor:
     * <ul>
     *     <li>Adds the CSS style class {@code "boardview-layer"}.</li>
     *     <li>Initializes the internal {@link ImageView}, adds it as a child node,
     *         and binds its {@code fitWidth} / {@code fitHeight} to this layer's
     *         width and height so that it fills the available area.</li>
     *     <li>Installs mouse listeners that delegate to the configured
     *         {@link ClickHandler} and {@link MoveHandler}, if present.</li>
     * </ul>
     * Subclasses can add further child nodes or event handlers in their own
     * constructors or initialization methods as needed.
     */
    public BoardViewLayer() {
        getStyleClass().add("boardview-layer");

        // Configure the backing image view to fill the layer by default.
        imageView.setPreserveRatio(false);
        imageView.setSmooth(true);
        imageView.fitWidthProperty().bind(widthProperty());
        imageView.fitHeightProperty().bind(heightProperty());

        getChildren().add(imageView);

        setOnMouseClicked(event -> {
            if (clickHandler != null) {
                clickHandler.onLayerMouseClicked(event);
            }
        });

        setOnMouseMoved(event -> {
            if (moveHandler != null) {
                moveHandler.onLayerMouseMoved(event);
            }
        });
    }
}

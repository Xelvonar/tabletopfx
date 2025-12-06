package tabletopfx.ui.layer;

import javafx.scene.image.Image;

/**
 * Visual layer responsible for rendering the static board background.
 *
 * <p>This layer sits at the bottom of the {@code BoardView} stack and is
 * typically used for terrain maps, hex grids, or other non-interactive
 * board artwork. It extends {@link BoardViewLayer}, inheriting:
 * <ul>
 *     <li>A dedicated {@code ImageView} used to display a single layer-wide image.</li>
 *     <li>Layer-level mouse callback support via
 *         {@link BoardViewLayer.ClickHandler} and
 *         {@link BoardViewLayer.MoveHandler}.</li>
 *     <li>The common CSS style class {@code "boardview-layer"}.</li>
 * </ul>
 *
 * <p>Client code can:
 * <ul>
 *     <li>Set the background art with {@link #setBackgroundImage(Image)}.</li>
 *     <li>Clear the background with {@link #clearBackgroundImage()}.</li>
 *     <li>Attach mouse handlers using
 *         {@link BoardViewLayer#setLayerClickHandler(BoardViewLayer.ClickHandler)}
 *         and
 *         {@link BoardViewLayer#setLayerMoveHandler(BoardViewLayer.MoveHandler)}
 *         to detect clicks or movement across the bare board surface.</li>
 * </ul>
 *
 * <p>The background is rendered beneath all other board layers (grid, pieces, UI),
 * so mouse events will only hit this layer in empty regions where no higher
 * layer consumes the event.</p>
 */
public class BackgroundLayer extends BoardViewLayer {

    /**
     * Constructs a new {@code BackgroundLayer}.
     *
     * <p>The constructor:
     * <ul>
     *     <li>Invokes {@link BoardViewLayer#BoardViewLayer()} to initialize
     *         the internal image view and mouse event wiring.</li>
     *     <li>Adds the CSS style class {@code "background-layer"} so that
     *         background-specific styling can be applied via stylesheets.</li>
     * </ul>
     *
     * <p>By default, no image is set and no mouse handlers are installed.
     * Callers should configure the layer after construction.</p>
     */
    public BackgroundLayer() {
        super();
        getStyleClass().add("background-layer");
    }

    /**
     * Assigns the image used as the board background.
     *
     * <p>This is a convenience wrapper around
     * {@link BoardViewLayer#setLayerImage(Image)} that makes the intent
     * explicit for callers using a {@code BackgroundLayer} instance.</p>
     *
     * @param img the background image to display, or {@code null} to clear it
     */
    public void setBackgroundImage(Image img) {
        setLayerImage(img);
    }

    /*
     * Note:
     * Mouse callbacks are inherited from BoardViewLayer:
     *
     *   setLayerClickHandler(BoardViewLayer.ClickHandler handler)
     *   setLayerMoveHandler(BoardViewLayer.MoveHandler handler)
     *
     * Example usage:
     *
     *   backgroundLayer.setLayerClickHandler(event -> {
     *       // Handle click on empty board area
     *   });
     *
     *   backgroundLayer.setLayerMoveHandler(event -> {
     *       // Handle hover over empty board area
     *   });
     */
}

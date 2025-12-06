package tabletopfx.ui.layer;

/**
 * Visual layer used for transient UI overlays such as selection rectangles,
 * hover highlights, drag outlines, or other non-persistent decorations that
 * sit above game pieces.
 *
 * <p>{@code UiLayer} extends {@link BoardViewLayer}, inheriting:
 * <ul>
 *     <li>A common base for board layers (styling, optional background image, mouse hooks).</li>
 *     <li>Layer-wide mouse callbacks via
 *         {@link BoardViewLayer#setLayerClickHandler(BoardViewLayer.ClickHandler)}
 *         and
 *         {@link BoardViewLayer#setLayerMoveHandler(BoardViewLayer.MoveHandler)}.</li>
 * </ul>
 *
 * <p>Unlike {@code BackgroundLayer} or {@code PieceLayer}, this layer is typically
 * populated with temporary nodes driven by UI state (selection, drag feedback,
 * hover effects) rather than domain objects.</p>
 */
public class UiLayer extends BoardViewLayer {

    /**
     * Constructs a new {@code UiLayer}.
     *
     * <p>The constructor:
     * <ul>
     *     <li>Invokes {@link BoardViewLayer#BoardViewLayer()} to initialize the
     *         base layer behavior.</li>
     *     <li>Adds the CSS style class {@code "ui-layer"} so UI-specific
     *         styling can be applied via stylesheets.</li>
     *     <li>Disables {@link #setPickOnBounds(boolean) pick-on-bounds} so that
     *         only the actual overlay nodes are mouse-interactive; empty regions
     *         of the layer do not block events from reaching underlying layers.</li>
     * </ul>
     */
    public UiLayer() {
        super();
        getStyleClass().add("ui-layer");

        // Only actual overlay nodes should be pickable; transparent areas
        // should allow events to pass through to layers below.
        setPickOnBounds(false);
    }
}

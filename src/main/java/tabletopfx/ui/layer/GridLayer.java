package tabletopfx.ui.layer;

/**
 * Visual layer responsible for grid-related overlays such as snap points,
 * named zones, and other map-aligned markers that sit above the background
 * but below pieces and UI decorations.
 *
 * <p>{@code GridLayer} extends {@link BoardViewLayer}, inheriting:
 * <ul>
 *     <li>Common layer styling and optional background image support.</li>
 *     <li>Layer-wide mouse callbacks via
 *         {@link BoardViewLayer#setLayerClickHandler(BoardViewLayer.ClickHandler)}
 *         and
 *         {@link BoardViewLayer#setLayerMoveHandler(BoardViewLayer.MoveHandler)}.</li>
 * </ul>
 *
 * <p>Typical responsibilities for this layer include:
 * <ul>
 *     <li>Rendering logical snap points used to align pieces.</li>
 *     <li>Displaying named zones, deployment areas, or scoring regions.</li>
 *     <li>Showing grid lines, coordinate labels, or other structural overlays
 *         tied to the underlying map.</li>
 * </ul>
 *
 * <p>Because this layer often contains sparse visual elements (points, lines,
 * small markers), its empty regions should not block mouse events from
 * reaching layers beneath it.</p>
 */
public class GridLayer extends BoardViewLayer {

    /**
     * Constructs a new {@code GridLayer}.
     *
     * <p>The constructor:
     * <ul>
     *     <li>Invokes {@link BoardViewLayer#BoardViewLayer()} to initialize
     *         base layer functionality.</li>
     *     <li>Adds the CSS style class {@code "grid-layer"} so grid-specific
     *         styling can be applied via stylesheets.</li>
     *     <li>Disables {@link #setPickOnBounds(boolean) pick-on-bounds} so that
     *         only actual grid-related nodes are mouse-interactive; empty
     *         portions of the layer allow events to pass through to
     *         underlying layers.</li>
     * </ul>
     */
    public GridLayer() {
        super();
        getStyleClass().add("grid-layer");
        setPickOnBounds(false);
    }
}

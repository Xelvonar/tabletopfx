package tabletopfx.ui.controller;

import javafx.scene.input.MouseEvent;
import tabletopfx.gamelogic.component.PieceComponent;
import tabletopfx.ui.layer.UiLayer;

/**
 * Strategy interface for UI modes.
 *
 * <p>Each {@code UiMode} implementation encapsulates the behavior of the UI
 * when the application is in a particular interaction state (e.g. waiting
 * for input, dragging a piece, resolving an action).</p>
 *
 * <p>{@link UiController} keeps a reference to the current mode and delegates
 * all mouse events to it. Modes may call back into {@link UiController}
 * to manipulate shared UI state and switch modes.</p>
 *
 * <p>All methods in this interface have default no-op implementations, so
 * modes only need to override the event types they care about. Unhandled
 * events are silently ignored.</p>
 */
public interface UiMode {

    //BackgroundLayer handlers

    /**
     * Called when the user clicks on the {@link tabletopfx.ui.layer.BackgroundLayer}.
     *
     * @param ctx   the owning {@link UiController}
     * @param event the mouse event
     */
    default void onBackgroundClick(UiController ctx, MouseEvent event) {
        // default: ignore
    }

    /**
     * Called when the mouse moves over the {@link tabletopfx.ui.layer.BackgroundLayer}.
     *
     * @param ctx   the owning {@link UiController}
     * @param event the mouse event
     */
    default void onBackgroundMove(UiController ctx, MouseEvent event) {
        // default: ignore
    }

    //Piece and PieceLayer handlers

    /**
     * Called when a piece's {@link javafx.scene.image.ImageView} in the
     * {@link tabletopfx.ui.layer.PieceLayer} is clicked.
     *
     * @param ctx   the owning {@link UiController}
     * @param piece the clicked piece
     * @param event the mouse event
     */
    default void onPieceClick(UiController ctx, PieceComponent piece, MouseEvent event) {
        // default: ignore
    }

    /**
     * Called when the user clicks on the {@link tabletopfx.ui.layer.PieceLayer}
     * but not on a piece (i.e. empty area).
     *
     * @param ctx   the owning {@link UiController}
     * @param event the mouse event
     */
    default void onPieceLayerClick(UiController ctx, MouseEvent event) {
        // default: ignore
    }

    //GridLayer handlers

    /**
     * Called when the user clicks on the {@link tabletopfx.ui.layer.GridLayer}.
     *
     * @param ctx   the owning {@link UiController}
     * @param event the mouse event
     */
    default void onGridClick(UiController ctx, MouseEvent event) {
        // default: ignore
    }

    /**
     * Called when the mouse moves over the {@link tabletopfx.ui.layer.GridLayer}.
     *
     * @param ctx   the owning {@link UiController}
     * @param event the mouse event
     */
    default void onGridMove(UiController ctx, MouseEvent event) {
        // default: ignore
    }

    //UiLayer handlers

    /**
     * Called when the mouse moves over the {@link UiLayer}.
     *
     * @param ctx   the owning {@link UiController}
     * @param event the mouse event
     */
    default void onUiLayerMove(UiController ctx, MouseEvent event) {
        // default: ignore
    }

    /**
     * Called when the user clicks on the {@link UiLayer}.
     *
     * @param ctx   the owning {@link UiController}
     * @param event the mouse event
     */
    default void onUiLayerClick(UiController ctx, MouseEvent event) {
        // default: ignore
    }
}

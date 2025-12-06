package tabletopfx.ui.controller;

import javafx.scene.input.MouseEvent;
import tabletopfx.gamelogic.component.PieceComponent;

/**
 * Default “idle” UI mode in which the controller is simply waiting for user input.
 *
 * <p>Typical responsibilities:
 * <ul>
 *     <li>Respond to initial piece clicks by selecting a piece and switching
 *         into a more specific mode (e.g. {@link PieceClickedMode}).</li>
 *     <li>Update status displays when hovering over the board.</li>
 * </ul>
 *
 * <p>All other events are ignored by default.</p>
 */
public final class WaitingMode implements UiMode {



    @Override
//    public void onBackgroundMove(UiController ctx, MouseEvent event) {
//    }

    @Override
    public void onPieceClick(UiController ctx, PieceComponent piece, MouseEvent event) {
        // Example: select the piece and transition into PIECE_CLICKED mode.
        ctx.selectPiece(piece, event);
        ctx.switchToPieceClickedMode(piece);
    }
}

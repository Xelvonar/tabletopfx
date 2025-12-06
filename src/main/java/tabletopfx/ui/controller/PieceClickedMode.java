package tabletopfx.ui.controller;

import javafx.scene.input.MouseEvent;
import tabletopfx.gamelogic.component.PieceComponent;

/**
 * UI mode in which a piece has been selected and the controller is expecting
 * follow-up input (e.g. a destination click, a target piece, or a cancel action).
 *
 * <p>Typical responsibilities:
 * <ul>
 *     <li>Interpret background clicks as move destinations.</li>
 *     <li>Interpret clicks on other pieces as interactions (attack, swap, etc.).</li>
 *     <li>Optionally render previews while the mouse moves.</li>
 * </ul>
 */
public final class PieceClickedMode implements UiMode {

    @Override
    public void onBackgroundClick(UiController ctx, MouseEvent event) {
        ctx.moveSelectedPieceTo(event.getX(), event.getY());
        ctx.switchToWaitingMode();
    }

//    @Override
//    public void onPieceClick(UiController ctx, PieceComponent piece, MouseEvent event) {
//        // Example: second piece clicked; handle interaction and return to waiting mode.
////        ctx.handleSecondPieceClick(piece);
////        ctx.switchToWaitingMode();
//    }

    @Override
    public void onBackgroundMove(UiController ctx, MouseEvent event) {
        ctx.previewMoveAt(event.getX(), event.getY());
    }
}

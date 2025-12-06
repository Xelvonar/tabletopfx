package tabletopfx.ui;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import tabletopfx.gamelogic.component.PieceComponent;
import tabletopfx.ui.layer.BackgroundLayer;

import java.util.function.Consumer;

/**
 * Root container for the entire UI.
 *
 * Layout:
 * - CENTER: BoardView (the main game board)
 * - LEFT:   Left panel (tools / palette / controls)
 * - RIGHT:  Right panel (info / inspector / log)
 * - BOTTOM: Bottom panel (cards / status / buttons)
 */
public class MainView extends BorderPane {

    private final BoardView boardView;
//    private final LeftPanel leftPanel;
//    private final RightPanel rightPanel;
//    private final BottomPanel bottomPanel;

    // event listeners that UiController will plug into


    public MainView() {

        this.boardView = createBoardView();
//        this.leftPanel = new LeftPanel();
//        this.rightPanel = new RightPanel();
//        this.bottomPanel = new BottomPanel();

        initializeLayout();

    }
    /*
        The methods below allow UiController to tell the various elements of MainView how to respond to clicks.
     */

    private void initializeLayout() {

        setCenter(boardView);
//        setLeft(leftPanel);
//        setRight(rightPanel);
//        setBottom(bottomPanel);
       setPadding(new Insets(8));
    }

    private BoardView createBoardView() {
        BoardView view = new BoardView();
        return view;
    }

    // Getters if you need to hook in controllers later:

    public BoardView getBoardView() {
        return boardView;
    }

//    public LeftPanel getLeftPanel() {
//        return leftPanel;
//    }
//
//    public RightPanel getRightPanel() {
//        return rightPanel;
//    }
//
//    public BottomPanel getBottomPanel() {
//        return bottomPanel;
//    }

}

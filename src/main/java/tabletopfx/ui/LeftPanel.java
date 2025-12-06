package tabletopfx.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * LeftPanel holds controls such as tools, palette items, or game-phase buttons.
 */

public class LeftPanel extends VBox {

    private final Label infoLabel = new Label("Move mouse over the board…");

    public LeftPanel() {
        configureLayout();
        populate();
    }

    private void configureLayout() {
        setSpacing(8);
        setPadding(new Insets(8));
        VBox.setVgrow(this, Priority.ALWAYS);
        getStyleClass().add("left-panel");
        setPrefWidth(200);
    }

    private void populate() {
        getChildren().add(infoLabel);
    }

    public void updatePointer(String panelName, double x, double y) {
        infoLabel.setText(panelName + "   x=" + String.format("%.1f", x)
                + "   y=" + String.format("%.1f", y));
    }
}

package tabletopfx.ui;

import javafx.geometry.Insets;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * RightPanel displays game information such as an inspector, turn log,
 * or contextual piece details.
 */

public class RightPanel extends VBox {

    private final TextArea logArea = new TextArea();

    public RightPanel() {
        configureLayout();
        populate();
    }

    private void configureLayout() {
        setSpacing(8);
        setPadding(new Insets(8));
        VBox.setVgrow(this, Priority.ALWAYS);
        getStyleClass().add("right-panel");
        setPrefWidth(200);
    }

    private void populate() {
        logArea.setEditable(false);
        logArea.setPrefRowCount(12);
        getChildren().add(logArea);
    }

    public void logClick(String panelName, double x, double y, MouseButton button) {
        String btn =
                button == MouseButton.PRIMARY ? "LEFT" :
                        button == MouseButton.SECONDARY ? "RIGHT" :
                                button.name();

        String msg = panelName + " " + btn +
                " click at x=" + String.format("%.1f", x) +
                ", y=" + String.format("%.1f", y) + "\n";

        logArea.appendText(msg);
    }
}


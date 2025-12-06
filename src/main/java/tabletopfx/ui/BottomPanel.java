package tabletopfx.ui;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * BottomPanel holds cards, a status bar, or action buttons.
 * Horizontal layout is standard here.
 */
public class BottomPanel extends HBox {

    public BottomPanel() {
        configureLayout();
        populate();
    }

    private void configureLayout() {
        setSpacing(8);
        setPadding(new Insets(8));
        HBox.setHgrow(this, Priority.ALWAYS);
        getStyleClass().add("bottom-panel");
        setPrefHeight(140);   // Optional: card-row height or status bar area
    }

    private void populate() {
        // Add card viewers, status messages, action buttons, etc.
        // Example placeholder:
        // Label status = new Label("Ready");
        // getChildren().add(status);
    }
}

package yplay.gui.attractor;

import javafx.application.Platform;
import yplay.core.data.ActiveVideoItem;
import yplay.gui.controller.MainController;

public class ActiveVideoAttractor {

    private MainController controller;

    public ActiveVideoAttractor(MainController controller) {
        this.controller = controller;
    }

    public void onLoaded(ActiveVideoItem loadedVideo) {
        Platform.runLater(() -> {
            controller.onActiveVideoSelected(loadedVideo);
        });
    }
}

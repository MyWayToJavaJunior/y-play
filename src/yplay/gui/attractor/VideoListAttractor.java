package yplay.gui.attractor;

import javafx.application.Platform;
import javafx.scene.control.ListView;
import yplay.core.data.VideoSearchResult;
import yplay.gui.GuiUtils;

public class VideoListAttractor {

    protected ListView guiVideoList;

    public VideoListAttractor(ListView guiVideoList) {
        this.guiVideoList = guiVideoList;
    }

    public void onGot(VideoSearchResult videoItemsList) {
        Platform.runLater(() -> {
            guiVideoList.getItems().clear();
            guiVideoList.getItems().addAll(videoItemsList.getItems());
            if (!guiVideoList.getItems().isEmpty()) {
                guiVideoList.scrollTo(0);
                guiVideoList.getSelectionModel().selectFirst();
                guiVideoList.getFocusModel().focus(0);
            }
            guiVideoList.requestFocus();
        });
    }

    public void onPageGot(VideoSearchResult pageContent) {
        Platform.runLater(() -> {
            int index = GuiUtils.getFirstVisibleItem(guiVideoList);
            guiVideoList.getItems().addAll(pageContent.getItems());
            guiVideoList.scrollTo(index + 1);
        });
    }
}

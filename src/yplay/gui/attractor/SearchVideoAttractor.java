package yplay.gui.attractor;

import javafx.application.Platform;
import javafx.scene.control.Accordion;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import yplay.core.data.VideoSearchResult;

public class SearchVideoAttractor extends VideoListAttractor {

    protected TitledPane guiContainer;
    protected Accordion containerAccordion;

    public SearchVideoAttractor(ListView guiSearchResult, TitledPane guiContainer, Accordion containerAccordion) {
        super(guiSearchResult);
        this.guiContainer = guiContainer;
        this.containerAccordion = containerAccordion;
    }

    public void onGot(VideoSearchResult searchResult) {
        Platform.runLater(() -> {
            guiVideoList.getItems().clear();
            guiVideoList.getItems().addAll(searchResult.getItems());
            if (!guiVideoList.getItems().isEmpty()) {
                guiVideoList.scrollTo(0);
                guiVideoList.getSelectionModel().selectFirst();
                guiVideoList.getFocusModel().focus(0);
            }
            containerAccordion.setExpandedPane(guiContainer);
            guiVideoList.requestFocus();
        });
    }
}

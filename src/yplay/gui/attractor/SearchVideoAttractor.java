package yplay.gui.attractor;

import javafx.application.Platform;
import javafx.scene.control.Accordion;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import yplay.core.data.VideoSearchResult;
import yplay.gui.GuiUtils;

public class SearchVideoAttractor {

    protected ListView guiSearchResult;
    protected TitledPane guiContainer;
    protected Accordion containerAccordion;

    public SearchVideoAttractor(ListView guiSearchResult, TitledPane guiContainer, Accordion containerAccordion) {
        this.guiSearchResult = guiSearchResult;
        this.guiContainer = guiContainer;
        this.containerAccordion = containerAccordion;
    }

    public void onFound(VideoSearchResult searchResult) {
        Platform.runLater(() -> {
            guiSearchResult.getItems().clear();
            guiSearchResult.getItems().addAll(searchResult.getItems());
            if (!guiSearchResult.getItems().isEmpty()) {
                guiSearchResult.scrollTo(0);
                guiSearchResult.getSelectionModel().selectFirst();
                guiSearchResult.getFocusModel().focus(0);
            }
            containerAccordion.setExpandedPane(guiContainer);
            guiSearchResult.requestFocus();
        });
    }

    public void onPageGot(VideoSearchResult pageContent) {
        Platform.runLater(() -> {
            int index = GuiUtils.getFirstVisibleItem(guiSearchResult);
            guiSearchResult.getItems().addAll(pageContent.getItems());
            guiSearchResult.scrollTo(index + 1);
        });
    }
}

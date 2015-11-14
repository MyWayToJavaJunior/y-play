package yplay.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;
import yplay.core.config.GlobalConfig;
import yplay.core.data.ActiveVideoItem;
import yplay.core.data.VideoListItem;
import yplay.core.data.VideoStream;
import yplay.core.fetcher.ActiveVideoFetcher;
import yplay.core.fetcher.ChannelContentFetcher;
import yplay.core.fetcher.SearchVideoFetcher;
import yplay.core.player.ExternalVideoPlayer;
import yplay.gui.attractor.ActiveVideoAttractor;
import yplay.gui.attractor.SearchVideoAttractor;
import yplay.gui.GuiUtils;
import yplay.gui.attractor.VideoListAttractor;

public class MainController {

    @FXML private ListView activeVideoRelated;
    @FXML private ImageView activeVideoThumbnail;
    @FXML private Label videoLength;
    @FXML private Label activeVideoTitle;
    @FXML private Label videoAuthor;
    @FXML private Label videoPublished;
    @FXML private TextArea videoDescription;
    @FXML private ListView videoTrack;
    @FXML private TextField searchString;
    @FXML private Button searchButton;
    @FXML private ListView searchResults;
    @FXML private ListView channelContent;
    @FXML private Accordion videoListAccordion;
    @FXML private TitledPane searchVideoResultContainer;
    @FXML private VBox activeImageMainContainer;

    // Fetchers
    private ChannelContentFetcher channelContentFetcher;
    private SearchVideoFetcher searchVideoFetcher;
    private ActiveVideoFetcher activeVideoFetcher;

    @FXML
    private void initialize() {
        initDefaultState();
        initActions();
        initAttractors();

        runDefaultActions();
    }

    private void initDefaultState() {
        searchString.setText(GlobalConfig.getInstance().getValue("search", "query"));
        activeImageMainContainer.setDisable(true);
        activeImageMainContainer.setVisible(false);
        activeImageMainContainer.setPrefSize(0, 0);
        videoDescription.setEditable(false);
    }

    private void initActions() {
        searchButton.setOnAction(event -> search());
        searchString.setOnAction(event -> search());

        searchResults.setOnScroll(this::onSearchListScroll);
        searchResults.setOnKeyTyped(this::onVideoListKeyTyped);
        searchResults.setOnKeyPressed(this::onSearchResultKeyPressed);
        searchResults.setOnMouseClicked(this::onVideoListMouseClicked);

        activeVideoThumbnail.setOnMouseClicked(this::onPlayClicked);
        activeVideoRelated.setOnKeyTyped(this::onVideoListKeyTyped);
        activeVideoRelated.setOnMouseClicked(this::onVideoListMouseClicked);

        videoTrack.setOnKeyTyped(this::onStreamListKeyTyped);

        channelContent.setOnScroll(this::onChannelContentScroll);
        channelContent.setOnKeyTyped(this::onVideoListKeyTyped);
        channelContent.setOnKeyPressed(this::onChannelContentKeyPressed);
        channelContent.setOnMouseClicked(this::onVideoListMouseClicked);
    }

    private void initAttractors() {
        SearchVideoAttractor searchVideoAttractor = new SearchVideoAttractor(searchResults, searchVideoResultContainer, videoListAccordion);
        searchVideoFetcher = new SearchVideoFetcher(searchVideoAttractor);

        ActiveVideoAttractor activeVideoAttractor = new ActiveVideoAttractor(this);
        activeVideoFetcher = new ActiveVideoFetcher(activeVideoAttractor);

        VideoListAttractor channelContentAttractor = new VideoListAttractor(channelContent);
        channelContentFetcher = new ChannelContentFetcher(channelContentAttractor);
    }

    private void enableActiveVideo() {
        activeImageMainContainer.setDisable(false);
        activeImageMainContainer.setVisible(true);
        activeImageMainContainer.setPrefSize(500, 10);
    }

    private void onSearchResultKeyPressed(KeyEvent event) {
        if (event.getCode().getName().toUpperCase().equals("DOWN")
                && GuiUtils.isLastElementSelected((ListView)event.getSource())) {
            getNextFoundPage();
        }
    }

    private void onChannelContentKeyPressed(KeyEvent event) {
        if (event.getCode().getName().toUpperCase().equals("DOWN")
                && GuiUtils.isLastElementSelected((ListView)event.getSource())) {
            channelContentFetcher.getNextPage();
        }
    }

    private void onVideoListKeyTyped(KeyEvent event) {
        if (event.getCharacter().equals("\r")) {
            VideoListItem selectedItem =  (VideoListItem)((ListView)(event.getSource())).getSelectionModel().getSelectedItem();
            selectVideo(selectedItem.getId());
        }
    }

    private void onVideoListMouseClicked(MouseEvent event) {
        if (event.getEventType().getName().toUpperCase().equals("MOUSE_CLICKED") && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            VideoListItem selectedItem =  (VideoListItem)((ListView)(event.getSource())).getSelectionModel().getSelectedItem();
            selectVideo(selectedItem.getId());
        }
    }

    private void onSearchListScroll(ScrollEvent event) {
        if (event.getTotalDeltaY() < 0 && GuiUtils.isLastElementVisible((ListView) event.getSource())) {
            getNextFoundPage();
        }
    }

    private void onChannelContentScroll(ScrollEvent event) {
        if (event.getTotalDeltaY() < 0 && GuiUtils.isLastElementVisible((ListView) event.getSource())) {
            channelContentFetcher.getNextPage();
        }
    }

    private void onPlayClicked(MouseEvent event) {
        if (event.getEventType().getName().toUpperCase().equals("MOUSE_CLICKED") && event.getButton() == MouseButton.PRIMARY) {
            VideoStream s = (VideoStream)videoTrack.getSelectionModel().getSelectedItem();
            if (s != null && s.getUrl().length() > 0) {
                playVideo(s.getUrl());
            }
        }
    }

    private void onStreamListKeyTyped(KeyEvent event) {
        if (event.getCharacter().equals("\r")) {
            VideoStream stream = (VideoStream)((ListView)(event.getSource())).getSelectionModel().getSelectedItem();
            playVideo(stream.getUrl());
        }
    }

    public void onActiveVideoSelected(ActiveVideoItem item) {
        activeVideoTitle.setText(item.getTitle());
        activeVideoThumbnail.setImage(new Image(item.getThumbnail()));
        videoLength.setText("Duration: " + item.getLength());
        videoAuthor.setText("Author: " + item.getAuthor());
        videoPublished.setText("Published: " + item.getPublishedAt());

        videoDescription.setText(item.getDescription());

        videoTrack.getItems().clear();
        videoTrack.getItems().addAll(item.getStreamList());
        videoTrack.getSelectionModel().selectFirst();

        activeVideoRelated.getItems().clear();
        activeVideoRelated.getItems().addAll(item.getRelatedVideo());

        enableActiveVideo();
        videoTrack.requestFocus();
        GlobalConfig.getInstance().putValue("video", "active", item.getId());
        getChannelContent(item.getChannelId());
    }

    private void search() {
        String query = searchString.getText();
        GlobalConfig.getInstance().putValue("search", "query", query);
        searchVideoFetcher.fetch(query);
    }

    private void getChannelContent(String channelId) {
        channelContentFetcher.fetch(channelId);
    }

    private void getNextFoundPage() {
        searchVideoFetcher.getNextPage();
    }

    private void playVideo(String videoStream) {
        new ExternalVideoPlayer().play(videoStream);
    }

    private void selectVideo(String videoId) {
        activeVideoFetcher.getVideo(videoId);
    }

    private void runDefaultActions() {
        search();
        String lastActiveVideo = GlobalConfig.getInstance().getValue("video", "active");
        if (lastActiveVideo.length() > 0) {
            selectVideo(lastActiveVideo);
        }
    }
}
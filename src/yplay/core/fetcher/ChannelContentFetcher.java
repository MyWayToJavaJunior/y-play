package yplay.core.fetcher;

import yplay.gui.attractor.VideoListAttractor;

public final class ChannelContentFetcher extends VideoListFetcher {
    private static final String URL = "http://i2dev.ru:8112/youtube-helper/channel/video?channel=";
    private String currentChannelId = "";

    public ChannelContentFetcher(VideoListAttractor attractor) {
        super(attractor);
    }

    public void fetch(String term) {
        if (term.length() > 0 && !term.equals(currentChannelId)) {
            currentChannelId = term;
            super.fetch(term);
        }
    }

    protected String constructFetchURL(String term) {
        return URL + term;
    }
}

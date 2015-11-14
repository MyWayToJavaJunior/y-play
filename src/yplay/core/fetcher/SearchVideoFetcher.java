package yplay.core.fetcher;

import yplay.gui.attractor.SearchVideoAttractor;

public final class SearchVideoFetcher extends VideoListFetcher {
    private static final String URL = "http://i2dev.ru:8112/youtube-helper/search/video?query=";

    public SearchVideoFetcher(SearchVideoAttractor attractor) {
        super(attractor);
    }

    protected String constructFetchURL(String term) {
        return URL + term;
    }
}

package yplay.core.fetcher;

import com.ning.http.client.*;
import yplay.core.data.VideoSearchResult;
import yplay.core.fetcher.request.RequestHandler;
import yplay.gui.attractor.SearchVideoAttractor;

public final class VideoListFetcher extends AbstractFetcher {
    private static final String URL = "http://i2dev.ru:8112/youtube-helper/search/video?query=";
    private SearchVideoAttractor attractor;
    private VideoSearchResult lastGotResult;
    private boolean nextPageInAction = false;
    String currentPageId = "";

    public VideoListFetcher(SearchVideoAttractor attractor) {
        this.attractor = attractor;
    }

    public void find(String term) {
        http.prepareGet(constructSearchURL(term)).execute(new RequestHandler() {
            @Override
            public Response onCompleted(Response response) throws Exception {
                lastGotResult = VideoSearchResult.createFromJsonText(getResponseText());
                attractor.onFound(lastGotResult);
                return response;
            }
        });
    }

    public void getNextPage() {
        if (nextPageInAction && !http.isClosed()) {
            return;
        }
        nextPageInAction = true;
        getPage(lastGotResult.getTerm(), lastGotResult.getNextPageToken());
    }

    private void getPage(String term, String page) {
        if (page.equals(currentPageId)) {
            return;
        }
        http.prepareGet(constructNextPageURL(term, page)).execute(new RequestHandler() {
            @Override
            public Response onCompleted(Response response) throws Exception {
                currentPageId = page;
                lastGotResult = VideoSearchResult.createFromJsonText(getResponseText());
                attractor.onPageGot(lastGotResult);
                nextPageInAction = false;
                return response;
            }

            public void onThrowable(Throwable t) {
                nextPageInAction = false;
            }
        });
    }

    private static String constructSearchURL(String term) {
        return URL + term;
    }

    private static String constructNextPageURL(String term, String pageId) {
        return constructSearchURL(term) + "&page=" + pageId;
    }
}

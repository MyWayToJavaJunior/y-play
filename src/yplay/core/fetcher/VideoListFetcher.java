package yplay.core.fetcher;

import com.ning.http.client.*;
import yplay.core.data.VideoSearchResult;
import yplay.core.fetcher.request.RequestHandler;
import yplay.gui.attractor.SearchVideoAttractor;
import yplay.gui.attractor.VideoListAttractor;

public abstract class VideoListFetcher extends AbstractFetcher {
    private VideoListAttractor attractor;
    private VideoSearchResult lastGotResult;
    private boolean nextPageInAction = false;
    String currentPageId = "";

    public VideoListFetcher(VideoListAttractor attractor) {
        this.attractor = attractor;
    }

    public void fetch(String term) {
        http.prepareGet(constructFetchURL(term)).execute(new RequestHandler() {
            @Override
            public Response onCompleted(Response response) throws Exception {
                lastGotResult = VideoSearchResult.createFromJsonText(getResponseText());
                attractor.onGot(lastGotResult);
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

    protected void getPage(String term, String page) {
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

    protected abstract String constructFetchURL(String term);

    protected String constructNextPageURL(String term, String pageId) {
        return constructFetchURL(term) + "&page=" + pageId;
    }
}

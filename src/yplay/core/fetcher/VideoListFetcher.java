package yplay.core.fetcher;

import yplay.core.data.VideoSearchResult;
import yplay.core.http.HttpClient;
import yplay.gui.attractor.VideoListAttractor;

public abstract class VideoListFetcher {
    private VideoListAttractor attractor;
    private VideoSearchResult lastGotResult;
    private boolean nextPageInAction = false;
    String currentPageId = "";

    public VideoListFetcher(VideoListAttractor attractor) {
        this.attractor = attractor;
    }

    public void fetch(String term) {
        new Thread() {
            public void run() {
                try {
                    String req = java.net.URLEncoder.encode(term, "UTF-8");
                    lastGotResult = VideoSearchResult.createFromJsonText(HttpClient.getHTML(constructFetchURL(req)));
                    attractor.onGot(lastGotResult);
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    //Nothing to do
                }
            }
        }.start();
    }

    public void getNextPage() {
        if (nextPageInAction) {
            return;
        }
        nextPageInAction = true;
        getPage(lastGotResult.getTerm(), lastGotResult.getNextPageToken());
    }

    protected void getPage(String term, String page) {
        if (page.equals(currentPageId)) {
            return;
        }
        new Thread() {
            public void run() {
                try {
                    currentPageId = page;
                    String eTerm = java.net.URLEncoder.encode(term, "UTF-8");
                    String ePage = java.net.URLEncoder.encode(page, "UTF-8");
                    lastGotResult = VideoSearchResult.createFromJsonText(HttpClient.getHTML(constructNextPageURL(eTerm, ePage)));
                    attractor.onPageGot(lastGotResult);
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    nextPageInAction = false;
                }
                nextPageInAction = false;
            }
        }.start();
    }

    protected abstract String constructFetchURL(String term);

    protected String constructNextPageURL(String term, String pageId) {
        return constructFetchURL(term) + "&page=" + pageId;
    }
}

package yplay.core.fetcher;

import yplay.core.data.ActiveVideoItem;
import yplay.core.http.HttpClient;
import yplay.gui.attractor.ActiveVideoAttractor;

public final class ActiveVideoFetcher {
    private static final String URL = "http://i2dev.ru:8112/youtube-helper/video?videoId=";
    private ActiveVideoItem lastGotResult;
    private ActiveVideoAttractor attractor;

    public ActiveVideoFetcher(ActiveVideoAttractor attractor) {
        this.attractor = attractor;
    }

    public void getVideo(String videoId) {
        if (lastGotResult != null && lastGotResult.getId().equals(videoId)) {
            return;
        }
        new Thread() {
            public void run() {
                try {
                    lastGotResult = ActiveVideoItem.createFromJsonText(HttpClient.getHTML(constructGetVideoURL(videoId)));
                    attractor.onLoaded(lastGotResult);
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    //Nothing to do
                }
            }
        }.start();
    }

    private static String constructGetVideoURL(String videoId) {
        return URL + videoId;
    }
}

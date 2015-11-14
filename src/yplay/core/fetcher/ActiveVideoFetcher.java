package yplay.core.fetcher;

import com.ning.http.client.Response;
import yplay.core.data.ActiveVideoItem;
import yplay.core.fetcher.request.RequestHandler;
import yplay.gui.attractor.ActiveVideoAttractor;

public final class ActiveVideoFetcher extends AbstractFetcher {
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

        http.prepareGet(constructGetVideoURL(videoId)).execute(new RequestHandler() {
            @Override
            public Response onCompleted(Response response) throws Exception {
                lastGotResult = ActiveVideoItem.createFromJsonText(getResponseText());
                attractor.onLoaded(lastGotResult);
                return response;
            }
        });
    }

    private static String constructGetVideoURL(String videoId) {
        return URL + videoId;
    }
}

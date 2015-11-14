package yplay.core.data;

import org.json.*;

import java.util.ArrayList;
import java.util.List;

public class VideoSearchResult {
    String term;
    String prevPageToken;
    String nextPageToken;
    List<VideoListItem> items;

    public VideoSearchResult() {
        this.items = new ArrayList<>();
    }

    public String getTerm() {
        return term;
    }

    public String getPrevPageToken() {
        return prevPageToken;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public List<VideoListItem> getItems() {
        return items;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public void setPrevPageToken(String prevPageToken) {
        this.prevPageToken = prevPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public void addVideo(VideoListItem item) {
        items.add(item);
    }

    public static VideoSearchResult createFromJsonText(String text) {
        VideoSearchResult result = new VideoSearchResult();
        try {
            JSONObject json = new JSONObject(text);
            result.setNextPageToken(json.getString("nextPageToken"));
            result.setTerm(json.getString("request"));
            JSONArray items = json.getJSONArray("videos");
            for (int i = 0; i < items.length(); i++) {
                VideoListItem v = new VideoListItem();
                v.setId(items.getJSONObject(i).getString("id"));
                v.setAuthor(items.getJSONObject(i).getString("author"));
                v.setChannelId(items.getJSONObject(i).getString("channelId"));
                v.setPublishedAt(items.getJSONObject(i).getString("publishedAt"));
                v.setThumbnail(items.getJSONObject(i).getString("thumbnail"));
                v.setTitle(items.getJSONObject(i).getString("title"));
                result.addVideo(v);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
}

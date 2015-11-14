package yplay.core.data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActiveVideoItem {
    String author;
    String channelId;
    String description;
    String id;
    String length;
    String publishedAt;
    String thumbnail;
    String title;
    List<VideoStream> streamList;
    List<VideoListItem> relatedVideo;

    public ActiveVideoItem() {
        streamList = new ArrayList<>(0);
        relatedVideo = new ArrayList<>(0);
    }

    public String getAuthor() {
        return author;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getLength() {
        return length;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public List<VideoStream> getStreamList() {
        return streamList;
    }

    public List<VideoListItem> getRelatedVideo() {
        return relatedVideo;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void addRelatedVideo(VideoListItem related) {
        relatedVideo.add(related);
    }

    public void addVideoStream(VideoStream stream) {
        streamList.add(stream);
    }

    public static ActiveVideoItem createFromJsonText(String text) {
        ActiveVideoItem result = new ActiveVideoItem();
        try {
            JSONObject json = new JSONObject(text);
            result.setId(json.getString("id"));
            result.setLength(json.getString("length"));
            result.setPublishedAt(json.getString("publishedAt"));
            result.setThumbnail(json.getString("thumbnail"));
            result.setTitle(json.getString("title"));
            result.setAuthor(json.getString("author"));
            result.setChannelId(json.getString("channelId"));
            result.setDescription(json.getString("description"));

            JSONArray related = json.getJSONArray("relatedVideos");
            for (int i = 0; i < related.length(); i++) {
                VideoListItem v = new VideoListItem();
                v.setId(related.getJSONObject(i).getString("id"));
                v.setAuthor(related.getJSONObject(i).getString("author"));
                v.setChannelId(related.getJSONObject(i).getString("channelId"));
                v.setPublishedAt(related.getJSONObject(i).getString("publishedAt"));
                v.setThumbnail(related.getJSONObject(i).getString("thumbnail"));
                v.setTitle(related.getJSONObject(i).getString("title"));
                result.addRelatedVideo(v);
            }

            JSONArray streams = json.getJSONArray("streamList");
            for (int i = 0; i < streams.length(); i++) {
                VideoStream s = new VideoStream();
                s.setQuality(streams.getJSONObject(i).getString("quality"));
                s.setType(streams.getJSONObject(i).getString("type"));
                s.setUrl(streams.getJSONObject(i).getString("url"));
                result.addVideoStream(s);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
}

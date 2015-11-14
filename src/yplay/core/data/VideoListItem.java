package yplay.core.data;

public class VideoListItem {
    String id;
    String author;
    String title;
    String channelId;
    String thumbnail;
    String length;
    String publishedAt;

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getLength() {
        return length;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String toString() {
        return this.title;
    }
}

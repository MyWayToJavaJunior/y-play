package yplay.core.data;

public class VideoStream {
    String quality;
    String url;
    String type;

    public String getQuality() {
        return quality;
    }

    public String getUrl() {
        return url;
    }

    public String getType() {
        return type;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toString() {
        return quality;
    }
}

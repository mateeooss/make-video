package com.makevideo.make_video.models.videoTemplate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.NoArgsConstructor;

@JsonTypeInfo( use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type" )
@NoArgsConstructor
public abstract class VideoTemplate {
    String title;

    int durationSeg;

    public VideoTemplate(@JsonProperty("title") String title, @JsonProperty("durationSeg") int durationSeg) {
        this.title = title;
        this.durationSeg = durationSeg;
    }
    public abstract void start();
    public abstract void renderVideo();
    public abstract void sendVideo();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDurationSeg() {
        return durationSeg;
    }

    public void setDurationSeg(int durationSeg) {
        this.durationSeg = durationSeg;
    }
}

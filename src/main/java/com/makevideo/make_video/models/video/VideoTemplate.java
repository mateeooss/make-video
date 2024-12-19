package com.makevideo.make_video.models.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonTypeInfo( use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type" )
@Getter
@Setter
@NoArgsConstructor
public abstract class VideoTemplate {
    String title;

    public VideoTemplate(@JsonProperty("title") String title) { this.title = title; }
    public abstract void start();
    public abstract void renderVideo();
    public abstract void sendVideo();
}

package com.makevideo.make_video.models.video.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.makevideo.make_video.models.video.VideoTemplate;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class VideoWithImages extends VideoTemplate {


    public VideoWithImages(@JsonProperty("title") String title) {
        super(title);
    }

    @Override
    public void start() {

    }

    @Override
    public void renderVideo() {

    }

    @Override
    public void sendVideo() {

    }
}

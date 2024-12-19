package com.makevideo.make_video.models.video.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.makevideo.make_video.models.video.VideoTemplate;
import com.makevideo.make_video.models.video.sentences.Sentences;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class VideoWithImages extends VideoTemplate {
    private String searchTerm;
    private String prefix;
    private String sourceContentOriginal;
    private String sourceContentSanitized;
    private List<Sentences> sentences;

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

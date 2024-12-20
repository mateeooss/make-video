package com.makevideo.make_video.models.video_template.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.makevideo.make_video.enums.LanguagesEnum;
import com.makevideo.make_video.models.video_template.VideoTemplate;
import com.makevideo.make_video.models.sentences.Sentences;
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
    private LanguagesEnum language;
    private List<LanguagesEnum> AdditionalLanguages;

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

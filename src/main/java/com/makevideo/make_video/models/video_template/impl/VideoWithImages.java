package com.makevideo.make_video.models.video_template.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.makevideo.make_video.enums.LanguagesEnum;
import com.makevideo.make_video.factorys.ServicesFactory;
import com.makevideo.make_video.models.video_template.VideoTemplate;
import com.makevideo.make_video.models.sentences.Sentences;
import com.makevideo.make_video.services.ChatGptService;
import com.makevideo.make_video.services.KeywordExtractorService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

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

    private ChatGptService chatGptService;
    private KeywordExtractorService extractorService;

    public VideoWithImages(@JsonProperty("title") String title) {
        super(title);
        this.chatGptService = ServicesFactory.getService(ChatGptService.class);
        this.extractorService = ServicesFactory.getService(KeywordExtractorService.class);
    }

    @Override
    public void start() {
        extractorService.getKeyWords("oi, sou Michael Jackson e gosto de fazer o movimento de dança moonwalk e p presidente inacio lula da silva, juntamente com akon foram para o planalto no distrito federal, no brasil e no brazil");
    }

    @Override
    public void renderVideo() {

    }

    @Override
    public void sendVideo() {

    }
}

package com.makevideo.make_video.models.videoTemplate.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.makevideo.make_video.enums.ChatGptRoleEnum;
import com.makevideo.make_video.enums.LanguagesEnum;
import com.makevideo.make_video.factorys.ServicesFactory;
import com.makevideo.make_video.models.chatGpt.MessageRequest;
import com.makevideo.make_video.models.chatGpt.Request;
import com.makevideo.make_video.models.chatGpt.Response;
import com.makevideo.make_video.models.videoTemplate.VideoTemplate;
import com.makevideo.make_video.models.sentences.Sentences;
import com.makevideo.make_video.services.ChatGptService;
import com.makevideo.make_video.services.FileHandlerService;
import com.makevideo.make_video.services.KeywordExtractorService;
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

    private ChatGptService chatGptService;
    private KeywordExtractorService extractorService;
    private FileHandlerService fileHandlerService;

    public VideoWithImages(@JsonProperty("title") String title) {
        super(title);
        this.chatGptService = ServicesFactory.getService(ChatGptService.class);
        this.extractorService = ServicesFactory.getService(KeywordExtractorService.class);
        this.fileHandlerService = ServicesFactory.getService(FileHandlerService.class);
    }

    @Override
    public void start() {
        Response response = new Response();
        fileHandlerService.readFile("teste.txt", String.class);
//        MessageRequest messageRequest = new MessageRequest(ChatGptRoleEnum.user.name(), "oi" + "bom dia");
//        Request request = new Request(List.of(messageRequest));
//
//        Response response = this.chatGptService.sendMessage(request);
//        extractorService.getKeyWords("oi, sou Michael Jackson e gosto de fazer o movimento de dança moonwalk e p presidente inacio lula da silva, juntamente com akon foram para o planalto no distrito federal, no brasil e no brazil");
    }

    @Override
    public void renderVideo() {

    }

    @Override
    public void sendVideo() {

    }
}

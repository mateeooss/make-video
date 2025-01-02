package com.makevideo.make_video.models.videoTemplate.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.makevideo.make_video.enums.LanguagesEnum;
import com.makevideo.make_video.factorys.ServicesFactory;
import com.makevideo.make_video.models.videoTemplate.VideoTemplate;
import com.makevideo.make_video.models.sentences.Sentences;
import com.makevideo.make_video.services.*;
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

    private SentenceService sentenceService;
    private ChatGptService chatGptService;
    private KeywordService keywordService;
    private FileHandlerService fileHandlerService;
    private ImageService imageService;

    public VideoWithImages(@JsonProperty("title") String title) {
        super(title);
        sentenceService = ServicesFactory.getService(SentenceService.class);
        chatGptService = ServicesFactory.getService(ChatGptService.class);
        keywordService = ServicesFactory.getService(KeywordService.class);
        fileHandlerService = ServicesFactory.getService(FileHandlerService.class);
        imageService = ServicesFactory.getService(ImageService.class);
    }

    @Override
    public void start() {
//        List<String> sentenceList = sentenceService.getSentences("Michael Jackson foi um dos artistas mais influentes e inovadores da história da música. Conhecido como o \"Rei do Pop\", ele conquistou o mundo com suas habilidades de dança, especialmente com o famoso \"moonwalk\", e sua voz única. Seu álbum Thriller é o mais vendido de todos os tempos, com sucessos como \"Billie Jean\" e \"Beat It\". Jackson também era um pioneiro em vídeos musicais, trazendo inovação com produções como \"Thriller\" e \"Smooth Criminal\". Além de sua música, ele teve um impacto significativo na cultura pop, moda e dança. Sua carreira foi marcada por grandes conquistas, mas também por controvérsias e desafios pessoais.");
        keywordService.getKeyWords("Michael Jackson foi um dos artistas mais influentes e inovadores da história da música. Conhecido como o Rei do Pop, ele conquistou o mundo com suas habilidades de dança, especialmente com o famoso moonwalk, e sua voz única. Seu álbum Thriller é o mais vendido de todos os tempos, com sucessos como Billie Jean e Beat It. Jackson também era um pioneiro em vídeos musicais, trazendo inovação com produções como Thriller e Smooth Criminal. Além de sua música, ele teve um impacto significativo na cultura pop, moda e dança. Sua carreira foi marcada por grandes conquistas, mas também por controvérsias e desafios pessoais.");
//        var a = imageService.searchImage();
//        System.out.println(a);
//        Response response = new Response();
//        System.out.println(fileHandlerService.readFile("teste.txt", String.class));
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

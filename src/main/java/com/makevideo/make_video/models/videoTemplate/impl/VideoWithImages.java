package com.makevideo.make_video.models.videoTemplate.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.makevideo.make_video.enums.ChatGptRoleEnum;
import com.makevideo.make_video.enums.LanguagesEnum;
import com.makevideo.make_video.factorys.ServicesFactory;
import com.makevideo.make_video.models.chatGpt.*;
import com.makevideo.make_video.models.videoTemplate.VideoTemplate;
import com.makevideo.make_video.models.sentences.Sentences;
import com.makevideo.make_video.services.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
public class VideoWithImages extends VideoTemplate {
    private String searchTerm;
    private String prefix;
    private String rawContentOriginal;
    private String sourceContentSanitized;
    private List<Sentences> sentences;
    private LanguagesEnum language;
    private List<LanguagesEnum> AdditionalLanguages;

    private SentenceService sentenceService;
    private ChatGptService chatGptService;
    private KeywordService keywordService;
    private FileService fileService;
    private ImageService imageService;
    private Request request;

    public VideoWithImages(@JsonProperty("title") String title) {
        super(title);
        setDurationSeg(240);
        sentenceService = ServicesFactory.getService(SentenceService.class);
        chatGptService = ServicesFactory.getService(ChatGptService.class);
        keywordService = ServicesFactory.getService(KeywordService.class);
        fileService = ServicesFactory.getService(FileService.class);
        imageService = ServicesFactory.getService(ImageService.class);
    }

    @Override
    public void start() {
        handleChatGptVideoScript();
//        List<String> sentenceList = sentenceService.getSentences("Michael Jackson foi um dos artistas mais influentes e inovadores da história da música. Conhecido como o \"Rei do Pop\", ele conquistou o mundo com suas habilidades de dança, especialmente com o famoso \"moonwalk\", e sua voz única. Seu álbum Thriller é o mais vendido de todos os tempos, com sucessos como \"Billie Jean\" e \"Beat It\". Jackson também era um pioneiro em vídeos musicais, trazendo inovação com produções como \"Thriller\" e \"Smooth Criminal\". Além de sua música, ele teve um impacto significativo na cultura pop, moda e dança. Sua carreira foi marcada por grandes conquistas, mas também por controvérsias e desafios pessoais.");
//        keywordService.getKeyWords("Michael Jackson foi um dos artistas mais influentes e inovadores da história da música. Conhecido como o Rei do Pop, ele conquistou o mundo com suas habilidades de dança, especialmente com o famoso moonwalk, e sua voz única. Seu álbum Thriller é o mais vendido de todos os tempos, com sucessos como Billie Jean e Beat It. Jackson também era um pioneiro em vídeos musicais, trazendo inovação com produções como Thriller e Smooth Criminal. Além de sua música, ele teve um impacto significativo na cultura pop, moda e dança. Sua carreira foi marcada por grandes conquistas, mas também por controvérsias e desafios pessoais.");
//        var a = imageService.searchImage();
//        System.out.println(a);
//        Response response = new Response();
//        System.out.println(fileHandlerService.readFile("teste.txt", String.class));

//        extractorService.getKeyWords("oi, sou Michael Jackson e gosto de fazer o movimento de dança moonwalk e p presidente inacio lula da silva, juntamente com akon foram para o planalto no distrito federal, no brasil e no brazil");
    }

    @Override
    public void renderVideo() {

    }

    @Override
    public void sendVideo() {

    }

    private void handleChatGptVideoScript(){
        assignRequest();
        assignRawContentOriginal();

    }

    private void assignRequest() {
        assignRequest();
        MessageRequest systemRequest = getSystemGptRequest();
        MessageRequest userRequest = getUserGptRequest();
        Request request = new Request(List.of(systemRequest, userRequest));

        setRequest(request);
    }

    private void assignRawContentOriginal() {
        Response response = this.chatGptService.sendMessage(getRequest());
        String rawContent = Optional.ofNullable(response.getChoices())
                            .map(choices -> choices.get(choices.size() - 1))
                            .map(Choice::getMessage)
                            .map(MessageResp::getContent)
                            .orElseThrow(() -> new RuntimeException("Erro ao capturar a mensagem retornada pelo ChatGpt"));

        setRawContentOriginal(rawContent);
//        fileService
    }

    private MessageRequest getSystemGptRequest(){
        return new MessageRequest(
            ChatGptRoleEnum.system.name(),
    "Você é um assistente especializado em criar roteiros. " +
            "Com base no tema e na duração fornecidos, elabore um roteiro detalhado, " +
            "incluindo introdução, desenvolvimento e conclusão, não se esqueça de solicitar like e " +
            "que a pessoa se inscreva no final do video. Certifique-se de ajustar " +
            "o conteúdo para caber no tempo especificado."
        );
    }

    private MessageRequest getUserGptRequest(){
        return new MessageRequest(
            ChatGptRoleEnum.user.name(),
            prefix+" "+searchTerm
        );
    }
}

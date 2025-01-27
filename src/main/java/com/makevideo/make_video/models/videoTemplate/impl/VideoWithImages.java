package com.makevideo.make_video.models.videoTemplate.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.makevideo.make_video.enums.ChatGptRoleEnum;
import com.makevideo.make_video.enums.LanguagesEnum;
import com.makevideo.make_video.factorys.ServicesFactory;
import com.makevideo.make_video.models.chatGpt.*;
import com.makevideo.make_video.models.googleApiTerm.TermUsed;
import com.makevideo.make_video.models.videoTemplate.VideoTemplate;
import com.makevideo.make_video.models.sentences.Sentence;
import com.makevideo.make_video.services.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.opencv.imgcodecs.Imgcodecs.imread;
import static org.opencv.imgcodecs.Imgcodecs.imwrite;
import static org.opencv.imgproc.Imgproc.cvtColor;

@Getter
@NoArgsConstructor
public class VideoWithImages extends VideoTemplate {
    static {
        // Carrega as bibliotecas nativas automaticamente
        Loader.load(opencv_imgcodecs.class);
    }

    private String search;
    private String searchTerm;
    private String rawContentOriginal;
    private String sourceContentSanitized;
    private List<Sentence> sentences;
    private LanguagesEnum language;
    private List<LanguagesEnum> AdditionalLanguages;
    private String directoryPath;

    @JsonIgnore
    private SentenceService sentenceService;
    @JsonIgnore
    private ChatGptService chatGptService;
    @JsonIgnore
    private KeywordService keywordService;
    @JsonIgnore
    private FileService fileService;
    @JsonIgnore
    private ImageService imageService;
    @JsonIgnore
    private PathStateService pathStateService;
    @JsonIgnore
    private Request request;
    @JsonIgnore
    private final Logger log = LoggerFactory.getLogger(VideoWithImages.class);

    public VideoWithImages(@JsonProperty("title") String title) {
        super(title, 240);
        sentenceService = ServicesFactory.getService(SentenceService.class);
        chatGptService = ServicesFactory.getService(ChatGptService.class);
        keywordService = ServicesFactory.getService(KeywordService.class);
        fileService = ServicesFactory.getService(FileService.class);
        imageService = ServicesFactory.getService(ImageService.class);
        pathStateService = ServicesFactory.getService(PathStateService.class);
    }

    @Override
    public void start() {
//        handleFile();
//        handleChatGptVideoScript();
//        handleSentences();
//        handleKeyWords();
        handleImg();
    }

    @Override
    public void renderVideo() {

    }

    @Override
    public void sendVideo() {

    }

    private void handleFile() {
        assignFilePath();
        createFile();
    }

    private void assignFilePath(){
        String directoryPath = pathStateService.basePath +"/"+this.search;
        pathStateService.directoryPath = directoryPath;
        pathStateService.txtDirectoryPath = directoryPath+"/"+this.search+".txt";

        setDirectoryPath(directoryPath);
    }

    private void createFile(){
        fileService.createIfNotExistAndSave(pathStateService.txtDirectoryPath, this);
    }

    private void saveFile(){
        fileService.saveFileAsString("D:/canal opniao/make-video-files/o casamento de Michael Jackson/o casamento de Michael Jackson.txt", this);
    }

    private void handleChatGptVideoScript(){
        assignRequest();
        assignRawContentOriginal();
    }

    private void assignRequest() {
        MessageRequest systemRequest = getSystemGptRequest();
        MessageRequest userRequest = getUserGptRequest();
        Request request = new Request(new ArrayList<>(List.of(systemRequest, userRequest)));
        setRequest(request);
        saveFile();
    }

    private void assignRawContentOriginal() {
        Response response = this.chatGptService.sendMessage(getRequest());
        String rawContent = Optional.ofNullable(response.getChoices())
                            .map(choices -> choices.get(choices.size() - 1))
                            .map(Choice::getMessage)
                            .map(MessageResp::getContent)
                            .orElseThrow(() -> new RuntimeException("Erro ao capturar a mensagem retornada pelo ChatGpt"));

        setRawContentOriginal(rawContent);
        saveFile();
    }

    private void handleSentences() {
       assignSentences();
    }

    private void assignSentences() {
        List<String> sentencesText = sentenceService.getSentences(getRawContentOriginal());

        Optional.ofNullable(sentencesText)
                .filter(sentences -> !sentences.isEmpty())
                .orElseThrow(() -> new IllegalStateException("a lista de sentences foram retornadas como vazias ou nullas"));

        List<Sentence> sentenceList = sentencesText.stream().map(Sentence::new).collect(Collectors.toList());
        setSentences(sentenceList);
        saveFile();
    }


    private MessageRequest getSystemGptRequest(){
        return new MessageRequest(
            ChatGptRoleEnum.system.name(),
                "Você é um assistente especializado em criar roteiros para vídeos, voce entrega apenas o roteiro, sem marcações " +
                        "Siga as instruções do usuário de forma clara e detalhada"
        );
    }

    private MessageRequest getUserGptRequest(){
        return new MessageRequest(
            ChatGptRoleEnum.user.name(),
            "Com base no tema e na duração fornecidos, elabore um roteiro detalhado. " +
                    "A introdução deve apresentar o tema de forma direta, sem minutagens. " +
                    "Não inclua tópicos como 'Desenvolvimento' ou 'Conclusão', entregue apenas o texto do video sem nenhuma marcação" +
                    ", nem minutagens no " +
                    "roteiro. Ao final, peça para o espectador dar like e se inscrever no canal." +
                    " Certifique-se de que o conteúdo se encaixe dentro do tempo especificado e " +
                    "forneça apenas o texto do roteiro, sem mais detalhes.\n " +
                    "tema: "+ search +
                    "\nduração: "+getDurationSeg()+" segundos"
        );
    }

    private void handleKeyWords(){
        assignKeyWords();
    }

    private void assignKeyWords(){
        var sentencesWithKeyWords = this.keywordService.getKeyWordsBySentenceList(getSentences());
        setSentences(sentencesWithKeyWords);
        saveFile();
    }

    private void handleImg() {
        assignImagesUrl();
        createImagesDirectory();
        downloadImages();
        resizeImages();
    }

    private void assignImagesUrl(){
        List<TermUsed> uniqueImageList = new ArrayList<>();

        getSentences().forEach(sentence -> {
            List<String> keyWords = getKeyWordsForSearch(sentence);

            sentence.getImagesUrl().addAll(
                imageService.searchUniqueImages(keyWords, 2, uniqueImageList)
            );
        });

        saveFile();
    }

    private void createImagesDirectory(){
        Path path = Path.of(getDirectoryPath()+"/video-images");

        fileService.createDirectories(path);
        pathStateService.imagesDirectoryPath = path.toString();
    }

    //todo corrigir o caminho ali quando liberar o fluxo
    private void downloadImages() {
        for (int indexSent = 0; indexSent < getSentences().size(); indexSent++) {
            var images = getSentences().get(indexSent).getImagesUrl();

            for (int imgIndex = 0; imgIndex < images.size(); imgIndex++) {
                try{
                    String image = images.get(imgIndex);
                    String imagesDirectory = pathStateService.imagesDirectoryPath;

                    fileService.downloadImage(image, imagesDirectory+"/sentenca-"+indexSent+"/image-"+imgIndex);
                } catch (Exception e){
                    log.warn("Erro ao baixar a imagem, seguindo o fluxo", e);
                }
            }
        }
        log.info("Download de imagens finalizado com sucesso!");
    }

    //todo corrigir esses caminhos
    private void resizeImages(){
        String directory = pathStateService.imagesDirectoryPath;
        log.info("Inicializando o resize das imagens");
        try {
            log.info("Inicializando a busca de todos caminhos de imagens do video");
            List<Path> imagesPath = Files.walk(Path.of(directory))
                                         .filter(path -> !Files.isDirectory(path)).toList();

            log.info("Caminhos das imagens do video foi gerado com sucesso!", imagesPath.toArray());

            imagesPath.forEach(imagePath -> {
                String inputPathString = imagePath.toString();

                String outputImagePath =
                        inputPathString.replaceAll("([\\\\/])([^\\\\/]+)(\\.[^.]+)$", "$1$2-converted$3");


                this.imageService.resizeWithBlurImage(imagePath.toString(), outputImagePath, 1920, 1080);
            });

            log.info("Todas imagens foram redimencionadas com sucesso!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> getKeyWordsForSearch(Sentence sentence){
        var keyWords = sentence.getKeyWords();

        return Optional.ofNullable(keyWords)
                .filter(keyWord -> !keyWord.isEmpty())
                .map(keyWord -> keyWord.size() > 1
                        ? List.of(searchTerm+" "+keyWord.getFirst(), searchTerm+" "+keyWord.getLast())
                        : List.of(searchTerm+" "+keyWord.getFirst(), searchTerm))
                .orElse(List.of(searchTerm, searchTerm));
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public void setRawContentOriginal(String rawContentOriginal) {
        this.rawContentOriginal = rawContentOriginal;
    }

    public void setSourceContentSanitized(String sourceContentSanitized) {
        this.sourceContentSanitized = sourceContentSanitized;
    }

    public void setSentences(List<Sentence> sentences) {
        this.sentences = sentences;
    }

    public void setLanguage(LanguagesEnum language) {
        this.language = language;
    }

    public void setAdditionalLanguages(List<LanguagesEnum> additionalLanguages) {
        AdditionalLanguages = additionalLanguages;
    }

    public void setSentenceService(SentenceService sentenceService) {
        this.sentenceService = sentenceService;
    }

    public void setChatGptService(ChatGptService chatGptService) {
        this.chatGptService = chatGptService;
    }

    public void setKeywordService(KeywordService keywordService) {
        this.keywordService = keywordService;
    }

    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    public void setImageService(ImageService imageService) {
        this.imageService = imageService;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public String getSearch() {
        return search;
    }

    public String getRawContentOriginal() {
        return rawContentOriginal;
    }

    public String getSourceContentSanitized() {
        return sourceContentSanitized;
    }

    public List<Sentence> getSentences() {
        return sentences;
    }

    public LanguagesEnum getLanguage() {
        return language;
    }

    public List<LanguagesEnum> getAdditionalLanguages() {
        return AdditionalLanguages;
    }

    public SentenceService getSentenceService() {
        return sentenceService;
    }

    public ChatGptService getChatGptService() {
        return chatGptService;
    }

    public KeywordService getKeywordService() {
        return keywordService;
    }

    public FileService getFileService() {
        return fileService;
    }

    public ImageService getImageService() {
        return imageService;
    }

    public Request getRequest() {
        return request;
    }

    public String getDirectoryPath() {
        return directoryPath;
    }

    public void setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
    }
}
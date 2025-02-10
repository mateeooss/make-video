package com.makevideo.make_video.models.videoTemplate.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.protobuf.ByteString;
import com.makevideo.make_video.enums.ChatGptRoleEnum;
import com.makevideo.make_video.enums.LanguagesEnum;
import com.makevideo.make_video.factorys.ServicesFactory;
import com.makevideo.make_video.models.chatGpt.*;
import com.makevideo.make_video.models.google.TermUsed;
import com.makevideo.make_video.models.google.TextToSpeech;
import com.makevideo.make_video.models.videoTemplate.VideoTemplate;
import com.makevideo.make_video.models.sentences.Sentence;
import com.makevideo.make_video.services.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

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
    private String directoryPathTxt;
    private String directoryPathMidia;

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
    private AudioService audioService;
    @JsonIgnore
    private Request request;
    @JsonIgnore
    private final String imageConvertedNomenclature = "converted";
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
        audioService = ServicesFactory.getService(AudioService.class);
    }

    @Override
    public void start() {
//        handleFile();
//        handleChatGptVideoScript();
        handleSentences();
        handleKeyWords();
        handleImg();
        handleAudio(); //todo retirar esse trecho dps, usar ele para testar a melhor voz
//        handleVideo();
    }

    @Override
    public void renderVideo() {

    }

    @Override
    public void sendVideo() {

    }

    private void handleFile() {
        assignFilePath();
        createDirectorys();
    }

    private void createDirectorys(){
        fileService.createDirectoriesByList(List.of(
                Path.of(directoryPath), Path.of(directoryPathMidia)
        ));
    }

    private void assignFilePath(){
        var rootPath = pathStateService.basePath +"/"+this.search;
        var txtPath = rootPath+"/"+"video_metadata.txt";
        var midiaPath = rootPath+"/midia";

        setDirectoryPath(rootPath);
        setDirectoryPathTxt(txtPath);
        setDirectoryPathMidia(midiaPath);
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
        saveTxt();
    }

    private void assignRawContentOriginal() {
        Response response = this.chatGptService.sendMessage(getRequest());
        String rawContent = Optional.ofNullable(response.getChoices())
                            .map(choices -> choices.get(choices.size() - 1))
                            .map(Choice::getMessage)
                            .map(MessageResp::getContent)
                            .orElseThrow(() -> new RuntimeException("Erro ao capturar a mensagem retornada pelo ChatGpt"));

        setRawContentOriginal(rawContent);
        saveTxt();
    }

    private void saveTxt(){
        fileService.saveFileAsString(getDirectoryPathTxt(), this);
    }

    private void handleSentences() {
       assignSentences();
    }

    private void assignSentences() {
        List<String> sentencesText = sentenceService.getSentences(getRawContentOriginal());
        List<Sentence> sentenceList = new ArrayList<>();

        Optional.ofNullable(sentencesText)
                .filter(sentences -> !sentences.isEmpty())
                .orElseThrow(() -> new IllegalStateException("a lista de sentences foram retornadas como vazias ou nullas"));


        for(int i = 0; i < sentencesText.size(); i++){
            String sentenceRootPath = getDirectoryPathMidia()+"/sentenca-"+i;
            String imagesPath = sentenceRootPath+"/images";
            String audioPath = sentenceRootPath+"/audio/audio.mp3";
            String videoPath = sentenceRootPath+"/video/video.mp4"; //todo verificar se vai ser essa extenção de video msm

            fileService.createDirectoriesByList(List.of(
                    Path.of(sentenceRootPath), Path.of(imagesPath), Path.of(audioPath).getParent(), Path.of(videoPath).getParent()
            ));

            sentenceList.add(new Sentence(
                    sentencesText.get(i),
                    sentenceRootPath,
                    imagesPath,
                    audioPath,
                    videoPath
            ));
        }

        setSentences(sentenceList);
        saveTxt();
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
        saveTxt();
    }

    private void handleImg() {
        assignImagesUrl();
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

        saveTxt();
    }

    //todo corrigir o caminho ali quando liberar o fluxo
    private void downloadImages() {
        for (int indexSent = 0; indexSent < getSentences().size(); indexSent++) {
            var sentence = getSentences().get(indexSent);
            var images = sentence.getImagesUrl();

            for (int imgIndex = 0; imgIndex < images.size(); imgIndex++) {
                try{
                    String image = images.get(imgIndex);

                    fileService.downloadImage(image, sentence.getImagesPath()+"/image-"+imgIndex);
                } catch (Exception e){
                    log.warn("Erro ao baixar a imagem, seguindo o fluxo", e);
                }
            }
        }
        log.info("Download de imagens finalizado com sucesso!");
    }

    private void resizeImages(){
        String directory = getDirectoryPathMidia();
        log.info("Inicializando o resize das imagens");
        try {
            log.info("Inicializando a busca de todos caminhos de imagens do video");
            List<Path> imagesPath = Files.walk(Path.of(getDirectoryPathMidia()))
                                         .filter(path -> {
                                             return !Files.isDirectory(path) || path.getFileName().toString().contains(imageConvertedNomenclature);
                                         }).toList();

            log.info("Caminhos das imagens do video foi gerado com sucesso!\n"+ Arrays.toString(imagesPath.toArray()));

            imagesPath.forEach(imagePath -> {
                String inputPathString = imagePath.toString();

                String outputImagePath =
                        inputPathString.replaceAll("([\\\\/])([^\\\\/]+)(\\.[^.]+)$", STR."$1$2-\{imageConvertedNomenclature}$3");


                this.imageService.resizeWithBlurImage(imagePath.toString(), outputImagePath, 1920, 1080);
            });

            log.info("Todas imagens foram redimencionadas com sucesso!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    //todo retirar o modelname e colocar dinamico assim como o language
    private void handleAudio(){
        AtomicBoolean erroOcorrido = new AtomicBoolean(false);

        this.getSentences().parallelStream().forEach(sentence -> {
            try{
                TextToSpeech textToSpeech =
                        TextToSpeech.builder()
                                .text(sentence.getText())
                                .languageCode(LanguagesEnum.PT_BR.getAcronym())
                                .modelName("pt-BR-Wavenet-E")
                                .voiceSpeed(1.10)
                                .SSML(false)
                                .voicePitch(2.80)
                                .build();

                ByteString audioBytes = this.audioService.textToSpeech(textToSpeech);

                this.fileService.createFile(audioBytes.toByteArray(), sentence.getAudioPath());
            } catch (Exception e){
                erroOcorrido.set(true);  // Marca que houve erro
                log.error("Erro ao processar a sentença: " + sentence.getText(), e);
            }
        });

        if (erroOcorrido.get()) {
            throw new RuntimeException("Erro ocorrido durante o processamento. Interrompendo a aplicação.");
        }
    }

    private void handleVideo(){
        try {
            // Caminho para o ffmpeg.exe
            String ffmpegPath = "src/main/resources/ffmpeg/bin/ffmpeg.exe";  // Ajuste o caminho conforme necessário

            // Defina o comando para criar um vídeo preto de 2 segundos
            String[] command = {
                    ffmpegPath,
                    "-f", "lavfi",
                    "-t", "2",
                    "-i", "color=c=black:s=1920x1080:r=30",
                    "-c:v", "libx264",
                    "-pix_fmt", "yuv420p",
                    "output_video.mp4"
            };

            // Inicia o processo
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);  // Mescla a saída de erro com a saída padrão
            Process process = processBuilder.start();

            // Captura a saída (sucesso e erro)
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            // Aguarda o término do processo
            int exitCode = process.waitFor();

            // Exibe a saída do processo
            if (exitCode == 0) {
                System.out.println("Vídeo preto de 2 segundos criado com sucesso!");
            } else {
                System.err.println("Erro ao criar o vídeo. Saída do FFmpeg:");
                System.err.println(output.toString());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
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

    public String getDirectoryPathTxt() {
        return directoryPathTxt;
    }

    public void setDirectoryPathTxt(String directoryPathTxt) {
        this.directoryPathTxt = directoryPathTxt;
    }

    public String getDirectoryPathMidia() {
        return directoryPathMidia;
    }

    public void setDirectoryPathMidia(String directoryPathMidia) {
        this.directoryPathMidia = directoryPathMidia;
    }
}
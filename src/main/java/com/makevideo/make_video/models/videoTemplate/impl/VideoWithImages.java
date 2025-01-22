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
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_core.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.opencv.imgcodecs.Imgcodecs.imread;
import static org.opencv.imgcodecs.Imgcodecs.imwrite;
import static org.opencv.imgproc.Imgproc.COLOR_BGR2GRAY;
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
        pathStateService.txtPath = directoryPath+"/"+this.search+".txt";

        setDirectoryPath(directoryPath);
    }

    private void createFile(){
        fileService.createIfNotExistAndSave(pathStateService.txtPath, this);
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
//        assignImagesUrl();
//        downloadImages();
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

    //todo corrigir o caminho ali quando liberar o fluxo
    private void downloadImages() {
        for (int indexSent = 0; indexSent < getSentences().size(); indexSent++) {
            var images = getSentences().get(indexSent).getImagesUrl();

            for (int imgIndex = 0; imgIndex < images.size(); imgIndex++) {
                try{
                    String image = images.get(imgIndex);
                    fileService.downloadImage(image, "D:/canal opniao/make-video-files/o casamento de Michael Jackson/imagesSent-"+indexSent+"/image-"+imgIndex);
                } catch (Exception e){
                    log.warn("Erro ao baixar a imagem, seguindo o fluxo", e);
                }
            }
        }
        log.info("Download de imagens finalizado com sucesso!");
    }

    private void resizeImages(){
//        String inputImagePath = "D:/canal opniao/make-video-files/o casamento de Michael Jackson/imagesSent-0/image-1.jpeg";  // Substitua pelo caminho correto
//        String outputImagePath = "D:/canal opniao/make-video-files/o casamento de Michael Jackson/imagesSent-0/image-1-converted.jpg"; // Caminho de saída
//
//        // Carregar a imagem
//        org.bytedeco.opencv.opencv_core.Mat image = opencv_imgcodecs.imread(inputImagePath);
//
//        // Verificar se a imagem foi carregada corretamente
//        if (image.empty()) {
//            System.out.println("Erro ao carregar a imagem.");
//            return;
//        }
//
//        // Converter para escala de cinza
//        Mat grayImage = new Mat();
//        opencv_imgproc.cvtColor(image, grayImage, opencv_imgproc.COLOR_BGR2GRAY);
//
//        // Salvar a imagem manipulada
//        boolean success = opencv_imgcodecs.imwrite(outputImagePath, grayImage);
//
//        if (success) {
//            System.out.println("Imagem salva com sucesso em " + outputImagePath);
//        } else {
//            System.out.println("Erro ao salvar a imagem.");
//        }




//        String inputImagePath = "D:/canal opniao/make-video-files/o casamento de Michael Jackson/imagesSent-0/image-1.jpeg";  // Substitua pelo caminho correto
//        String outputImagePath = "D:/canal opniao/make-video-files/o casamento de Michael Jackson/imagesSent-0/image-1-converted.jpg"; // Caminho de saída
//
//        // Ler a imagem original
//        Mat original = opencv_imgcodecs.imread(inputImagePath);
//        if (original.empty()) {
//            log.error("Erro ao carregar a imagem!");
//            return;
//        }
//
//        // Tamanho da nova imagem (Full HD)
//        int fullHDWidth = 1920;
//        int fullHDHeight = 1080;
//
//        // Criar o plano de fundo (imagem borrada)
//        Mat blurredBackground = new Mat();
//        opencv_imgproc.resize(original, blurredBackground, new Size(fullHDWidth, fullHDHeight)); // Redimensionar
//        opencv_imgproc.GaussianBlur(blurredBackground, blurredBackground, new Size(55, 55), 30); // Aplicar desfoque
//
//        // Calcular a posição central para colocar a imagem original
//        int centerX = (fullHDWidth - original.cols()) / 2;
//        int centerY = (fullHDHeight - original.rows()) / 2;
//
//        // Copiar a imagem original no centro da imagem borrada
//        Rect roi = new Rect(centerX, centerY, original.cols(), original.rows());
//        Mat regionOfInterest = blurredBackground.apply(roi); // Região onde será colocada a imagem original
//        original.copyTo(regionOfInterest); // Copiar imagem original para a região
//
//        // Salvar a nova imagem
//        opencv_imgcodecs.imwrite(outputImagePath, blurredBackground);
//        System.out.println("Imagem processada e salva em " + outputImagePath);

        //estava funcionado
//        String inputImagePath = "D:/canal opniao/make-video-files/o casamento de Michael Jackson/imagesSent-0/image-1.jpeg";  // Caminho da imagem original
//        String outputImagePath = "D:/canal opniao/make-video-files/o casamento de Michael Jackson/imagesSent-0/image-1-converted.jpg"; // Caminho da imagem de saída
//



//// Ler a imagem original




//        Mat original = opencv_imgcodecs.imread(inputImagePath);
//        if (original.empty()) {
//            log.error("Erro ao carregar a imagem!");
//            return;
//        }
//
//// Tamanho da nova imagem (Full HD)
//        int fullHDWidth = 1920;
//        int fullHDHeight = 1080;
//
//// Calcular a proporção de redimensionamento para cobrir toda a área sem distorção
//        double aspectRatio = (double) original.cols() / original.rows();
//        int newWidth, newHeight;
//
//// Ajuste para cobrir totalmente a área Full HD sem distorção, com corte
//        if (fullHDWidth / aspectRatio > fullHDHeight) {
//            newWidth = fullHDWidth;
//            newHeight = (int) (fullHDWidth / aspectRatio);  // A altura é ajustada para manter a proporção
//        } else {
//            newHeight = fullHDHeight;
//            newWidth = (int) (fullHDHeight * aspectRatio);  // A largura é ajustada para manter a proporção
//        }
//
//// Redimensionar a imagem para preencher Full HD proporcionalmente
//        Mat resizedBackground = new Mat();
//        opencv_imgproc.resize(original, resizedBackground, new Size(newWidth, newHeight));
//
//// Criar uma nova imagem Full HD para o fundo
//        Mat finalImage = new Mat(new Size(fullHDWidth, fullHDHeight), original.type());
//
//// Calcular a posição para centralizar a imagem de fundo redimensionada
//        int centerX = (fullHDWidth - newWidth) / 2;
//        int centerY = (fullHDHeight - newHeight) / 2;
//
//// Copiar a imagem redimensionada para a posição central do fundo Full HD
//        Mat roi = finalImage.apply(new Rect(centerX, centerY, resizedBackground.cols(), resizedBackground.rows()));
//        resizedBackground.copyTo(roi);
//
//// Salvar a imagem final
//        opencv_imgcodecs.imwrite(outputImagePath, finalImage);




        String inputImagePath = "D:/canal opniao/make-video-files/o casamento de Michael Jackson/imagesSent-0/image-1.jpeg";
        // Caminho da imagem de saída
        String outputImagePath = "D:/canal opniao/make-video-files/o casamento de Michael Jackson/imagesSent-0/image-1-converted.jpg";

        // Ler a imagem original
        Mat original = opencv_imgcodecs.imread(inputImagePath);
        if (original.empty()) {
            System.err.println("Erro ao carregar a imagem!");
            return;
        }

        // Tamanho da nova imagem (Full HD)
        int fullHDWidth = 1920;
        int fullHDHeight = 1080;

        // Calcular novo tamanho proporcional para preencher sem distorcer
        int originalWidth = original.cols();
        int originalHeight = original.rows();

        // Calcula a proporção para escalar a imagem
        double scaleWidth = (double) fullHDWidth / originalWidth;
        double scaleHeight = (double) fullHDHeight / originalHeight;
        double scale = Math.max(scaleWidth, scaleHeight); // Aumentar para cobrir completamente

        int newWidth = (int) (originalWidth * scale);
        int newHeight = (int) (originalHeight * scale);

        // Redimensionar a imagem
        Mat resizedImage = new Mat();
        opencv_imgproc.resize(original, resizedImage, new Size(newWidth, newHeight));

        // Criar uma nova imagem de fundo em tamanho Full HD
        Mat fullHDImage = new Mat(new Size(fullHDWidth, fullHDHeight), original.type(), new Scalar(0, 0, 0, 0));

        // Centralizar a imagem redimensionada no fundo
        int xOffset = (newWidth - fullHDWidth) / 2;
        int yOffset = (newHeight - fullHDHeight) / 2;

        Rect roi = new Rect(xOffset, yOffset, fullHDWidth, fullHDHeight);
        Mat croppedImage = new Mat(resizedImage, roi);
        opencv_imgproc.GaussianBlur(croppedImage, croppedImage, new Size(55, 55), 30);
        int centerX = (fullHDWidth - original.cols()) / 2;
        int centerY = (fullHDHeight - original.rows()) / 2;
                // Copiar a imagem original no centro da imagem borrada
        Rect roi2 = new Rect(centerX, centerY, original.cols(), original.rows());
        Mat regionOfInterest = croppedImage.apply(roi2); // Região onde será colocada a imagem original
        original.copyTo(regionOfInterest); // Copiar imagem original para a região

        // Salvar a imagem final
        opencv_imgcodecs.imwrite(outputImagePath, croppedImage);
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
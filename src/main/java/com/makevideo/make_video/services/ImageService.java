package com.makevideo.make_video.services;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.customsearch.v1.CustomSearchAPI;
import com.google.api.services.customsearch.v1.model.Result;
import com.google.api.services.customsearch.v1.model.Search;
import com.makevideo.make_video.exception.NoImagesFoundException;
import com.makevideo.make_video.models.google.TermUsed;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Rect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Validated
public class ImageService {

    @Value("${google.apikey}")
    private String keyapi;

    @Value("${google.searchEngine.id}")
    private String cx;

    private final JsonFactory JSON_FACTORY =  GsonFactory.getDefaultInstance();
    Logger log = LoggerFactory.getLogger(ImageService.class);

    public List<String> searchImages(
            @NotNull @Size(min = 1) List<String> searchTermList,
            @NotNull @Min(1) Integer numResultPerTerm
    ){
        List<String> imagesList = new ArrayList<>();
        searchTermList.forEach( term -> imagesList.addAll(searchImage(term, numResultPerTerm, 1)));
        return imagesList;
    }

    public List<String> searchUniqueImages(
            @NotNull @Size(min = 1) List<String> searchTermList,
            @NotNull @Min(1) Integer numResultPerTerm,
            List<TermUsed> uniqueImageList
    ){
        List<String> imagesList = new ArrayList<>();

        searchTermList.forEach( term ->{
            uniqueImageList.stream()
                    .filter(termUse -> termUse.getTerm().equals(term))
                    .findFirst()
                    .ifPresentOrElse(termUsed -> {
                        termUsed.setManyTimes(termUsed.getManyTimes() + numResultPerTerm);
                        var indexStart = termUsed.getManyTimes();
                        imagesList.addAll(searchImage(term, numResultPerTerm, indexStart));
                    }, () -> {
                        imagesList.addAll(searchImage(term, numResultPerTerm, 1));
                        uniqueImageList.add(new TermUsed(term, 1));
                    });
        });

        return imagesList;
    }

    public List<String> searchImage(
            @NotNull @Size(min = 1) String searchTerm,
            @NotNull @Min(1) Integer numResult,
            @NotNull Integer indexStart
    ){
        try {
            var cse = new CustomSearchAPI.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY, null)
                .setApplicationName("makeVideo").build().cse().list();

            cse.setKey(keyapi);
            cse.setCx(cx);
            cse.setSearchType("image");
            cse.setQ(searchTerm);
            cse.setStart(indexStart.longValue());
            cse.setNum(numResult);

            log.info("Iniciando pesquisa de images no google:\n {}", cse);
            Search search = cse.execute();
            log.info("Pesquisa de imagens realizada com sucesso:\n");

            var links = Optional.ofNullable(search.getItems())
                    .orElseThrow(() -> new NoImagesFoundException("ImageService: Custom Search retornou 0 imagens"))
                    .stream().map(Result::getLink)
                    .collect(Collectors.toList());
            log.info("imagens retornadas\nquery: {}\nimages: {}", cse.getQ(), links);

            return links;
        } catch (GeneralSecurityException | IOException e) {
            throw new NoImagesFoundException("ImageService: erro ao buscar imagens com o custom search", e);
        }
    }

    public void resizeWithBlurImage(String inputImagePath, String outputImagePath, int width, int height) {
        try {
            log.info("Inicializando o resize com blur para {}x{} da imagem: {}", width, height, inputImagePath);

            // Carregar imagem original
            Mat original = opencv_imgcodecs.imread(inputImagePath);
            if (original.empty()) {
                throw new RuntimeException("Erro ao carregar a imagem: " + inputImagePath);
            }

            int originalWidth = original.cols();
            int originalHeight = original.rows();
            int fullHDWidth = width;
            int fullHDHeight = height;

            // Calcular escala proporcional
            double scaleWidth = (double) fullHDWidth / originalWidth;
            double scaleHeight = (double) fullHDHeight / originalHeight;

            // Arredondamento correto
            double scale = Math.max(scaleWidth, scaleHeight);

            int newWidth = (int) Math.ceil(originalWidth * scale);
            int newHeight = (int) Math.ceil(originalHeight * scale);

            // Garantir que não fiquem menores que Full HD
            newWidth = Math.max(newWidth, fullHDWidth);
            newHeight = Math.max(newHeight, fullHDHeight);

            // Redimensionar a imagem
            Mat resizedImage = new Mat();
            opencv_imgproc.resize(original, resizedImage,  new org.bytedeco.opencv.opencv_core.Size(newWidth, newHeight));

            // Centralizar
            int xOffset = (newWidth - fullHDWidth) / 2;
            int yOffset = (newHeight - fullHDHeight) / 2;

            // Garantir que não extrapole
            xOffset = Math.max(0, xOffset);
            yOffset = Math.max(0, yOffset);

            Rect roi = new Rect(xOffset, yOffset, fullHDWidth, fullHDHeight);
            Mat croppedImage = new Mat(resizedImage, roi);
            opencv_imgproc.GaussianBlur(croppedImage, croppedImage,  new org.bytedeco.opencv.opencv_core.Size(55, 55), 30);

            // Copiar imagem original proporcionalmente no centro da imagem final
            double minScale = Math.min(scaleWidth, scaleHeight);
            int finalWidth = (int) (originalWidth * minScale);
            int finalHeight = (int) (originalHeight * minScale);

            int centerX = (fullHDWidth - finalWidth) / 2;
            int centerY = (fullHDHeight - finalHeight) / 2;

            Mat scaledOriginal = new Mat();
            opencv_imgproc.resize(original, scaledOriginal, new org.bytedeco.opencv.opencv_core.Size(finalWidth, finalHeight));

            Rect roi2 = new Rect(centerX, centerY, finalWidth, finalHeight);
            Mat regionOfInterest = croppedImage.apply(roi2);
            scaledOriginal.copyTo(regionOfInterest);

            // Salvar
            opencv_imgcodecs.imwrite(outputImagePath, croppedImage);
            log.info("Imagem redimensionada com blur salva em: {}", outputImagePath);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao redimensionar a imagem: " + inputImagePath, e);
        }
    }
}

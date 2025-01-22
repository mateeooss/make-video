package com.makevideo.make_video.services;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.customsearch.v1.CustomSearchAPI;
import com.google.api.services.customsearch.v1.model.Result;
import com.google.api.services.customsearch.v1.model.Search;
import com.makevideo.make_video.exception.NoImagesFoundException;
import com.makevideo.make_video.models.googleApiTerm.TermUsed;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Validated
public class ImageService {

    @Value("${google.customSearch.apikey}")
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


}

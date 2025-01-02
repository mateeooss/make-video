package com.makevideo.make_video.services;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.customsearch.v1.CustomSearchAPI;
import com.google.api.services.customsearch.v1.model.Result;
import com.google.api.services.customsearch.v1.model.Search;
import com.makevideo.make_video.exception.NoImagesFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ImageService {

    @Value("${google.customSearch.apikey}")
    private String keyapi;

    @Value("${google.searchEngine.id}")
    private String cx;

    private final JsonFactory JSON_FACTORY =  GsonFactory.getDefaultInstance();

    public List<String> searchImage(){
        try {
            var cse = new CustomSearchAPI.Builder( GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY, null)
                .setApplicationName("makeVideo").build().cse().list();

            cse.setKey(keyapi);
            cse.setCx(cx);
            cse.setSearchType("image");
            cse.setQ("Michael Jackson");
            cse.setNum(2);

            Search search = cse.execute();
            var links = Optional.ofNullable(search.getItems())
                    .orElseThrow(() -> new NoImagesFoundException("ImageService: Custom Search retornou 0 imagens")).stream()
                    .map(Result::getLink)
                    .collect(Collectors.toList());

            return links;
        } catch (GeneralSecurityException | IOException e) {
            throw new NoImagesFoundException("ImageService: erro ao buscar imagens com o custom search", e.getCause());
        }
    }
}

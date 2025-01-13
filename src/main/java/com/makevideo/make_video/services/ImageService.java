package com.makevideo.make_video.services;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.customsearch.v1.CustomSearchAPI;
import com.google.api.services.customsearch.v1.model.Result;
import com.google.api.services.customsearch.v1.model.Search;
import com.makevideo.make_video.exception.NoImagesFoundException;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
public class ImageService {

    @Value("${google.customSearch.apikey}")
    private String keyapi;

    @Value("${google.searchEngine.id}")
    private String cx;

    private FileService fileService;

    private final JsonFactory JSON_FACTORY =  GsonFactory.getDefaultInstance();
    Logger log = LoggerFactory.getLogger(ImageService.class);

    @Autowired
    public ImageService(FileService fileService) {
        this.fileService = fileService;
    }

    public List<String> searchImages(List<String> searchTermList){
        List<String> imagesList = new ArrayList<>();
        searchTermList.forEach( term -> imagesList.addAll(searchImage(term)));
        return imagesList;
    }

    public List<String> searchImage(String searchTerm){
        try {
            var cse = new CustomSearchAPI.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY, null)
                .setApplicationName("makeVideo").build().cse().list();

            cse.setKey(keyapi);
            cse.setCx(cx);
            cse.setSearchType("image");
            cse.setQ(searchTerm);
            cse.setNum(2);

            log.info("Iniciando pesquisa de images no google:\n {}", cse);
            Search search = cse.execute();
            log.info("Pesquisa de imagens realizada com sucesso:\n");

            var links = Optional.ofNullable(search.getItems())
                    .orElseThrow(() -> new NoImagesFoundException("ImageService: Custom Search retornou 0 imagens")).stream()
                    .map(Result::getLink)
                    .collect(Collectors.toList());
            log.info("imagens retornadas\nquery: {}\nimages: {}", cse.getQ(), links);

            return links;
        } catch (GeneralSecurityException | IOException e) {
            throw new NoImagesFoundException("ImageService: erro ao buscar imagens com o custom search", e.getCause());
        }
    }

    public void downloadImage(String imageUrl, String path) {
        try{
            URI uri = URI.create(imageUrl);
            URL url = uri.toURL();
            try (InputStream image = url.openStream()) {
                this.fileService.saveImage(image, path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Erro ao cria URL da imagem:\nimagem: "+imageUrl+"\nerro:", e);
        }
    }
}

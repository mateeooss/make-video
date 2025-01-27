package com.makevideo.make_video.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileService {
    ObjectMapper mapper = new ObjectMapper();
    Logger log = LoggerFactory.getLogger(FileService.class);

    public <T> T readFile(String uri, Class<T> clazz){
        try {
            Path path = Path.of(uri);
            log.info("Iniciando leitura do arquivo no caminho {}", uri);

            String content = Files.readString(path);
            log.info("Arquivo lido com sucesso ");

            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            log.info("Iniciando a conversão do arquivo para o objeto {} ", clazz);
            var file = mapper.readValue(content, clazz);
            log.info("Arquivo convertido com sucesso");

            return file;
        } catch (IOException e) {
            throw new RuntimeException("erro ao ler o arquivo no diretorio informado: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("erro ao realizar o parse de json para texto: " + e.getMessage());
        }
    }


    public List<Path> getPathListBydirectory(String directory) throws NoSuchFieldException {
        Path pathDirectory = Path.of(directory);

        try (Stream<Path> paths = Files.list(pathDirectory)) {
            if (!Files.isDirectory(pathDirectory) || !Files.isReadable(pathDirectory)) {
                throw new NoSuchFieldException("O diretório não é válido");
            }

            return paths.filter(Files::isRegularFile).toList();
        } catch (NoSuchFieldException | IOException e) {
          log.info("Erro ao ler as imagens do diretorio");
          throw new NoSuchFieldException("O diretorio não é valido");
        }
    }

    public <T> void saveFileAsString(String uri, T object){
        try {
            Path path = Path.of(uri);
            if(Files.notExists(path)) throw new RuntimeException("caminho do arquivo inexistente: ");

            log.info("Iniciando a conversão do objecto para json em String");
            String fileString = mapper.writeValueAsString(object);
            log.info("Objeto convertido com sucesso \n{}", fileString);

            log.info("Iniciando o save do json no caminho: {}", uri);
            Files.writeString(path, fileString);
            log.info("Save realizado com sucesso");
        } catch (IOException e) {
            throw new RuntimeException("erro ao escrever o arquivo no diretorio informado: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("erro ao realizar o parse de json para texto: " + e.getMessage());
        }
    }

    public void downloadImage(String imageUrl, String path) throws IOException {
//        path = path + ".png";
        log.info("Abrindo conecção para download da imagem URL: {}", imageUrl);
        try {
            URL url = new URL(imageUrl);
            URLConnection connection =  url.openConnection();
            connection.setConnectTimeout(6000);
            connection.setReadTimeout(6000);
            path = path +"."+getFileExtension(url);

            try (InputStream image = connection.getInputStream()) {
                saveImage(image, path);
            }
        } catch (IOException e) {
            throw e;
        }
    }

    public void saveImage(InputStream image, String pathToSafe){
        try {
            Path path = Path.of(pathToSafe);
            createDirectories(path);
            log.info("Iniciando o save da imagem no caminho {}", pathToSafe);
            Files.copy(image, path, StandardCopyOption.REPLACE_EXISTING);
            log.info("Save realizado com sucesso!");
        } catch (IOException e) {
            throw new RuntimeException("erro ao salvar a imagem no diretorio informado: ", e);
        }
    }

    public void createFile(String uri){
        try {
            if(existFile(uri)) throw new RuntimeException("O arquivo ja existe: ");

            Path path = Path.of(uri);
            log.info("Criando estrutura de pastas caso não exista");
            createDirectories(path);
            log.info("Pastas inicializadas com sucesso");
            log.info("Iniciando a criação do arquivo no caminho: {}", uri);
            Files.createFile(path);
            log.info("Arquivo criado com sucesso: {}", uri);
        } catch (IOException e) {
            throw new RuntimeException("erro ao escrever o arquivo no diretorio informado: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("erro ao realizar o parse de json para texto: " + e.getMessage());
        }
    }

    public void createDirectories(Path path){
        try{
            Files.createDirectories(path.getParent());
        } catch (IOException e) {
            throw new RuntimeException("erro ao criar a pasta no diretorio informado: " + e.getMessage());
        }
    }

    public <T> void createIfNotExistAndSave(String uri, T object){
        if(notExistFile(uri)) this.createFile(uri);
        this.saveFileAsString(uri, object);
    }

    public Boolean existFile(String uri){
        Path path = Path.of(uri);
        return Files.exists(path);
    }

    public Boolean notExistFile(String uri){
        return !existFile(uri);
    }

    private String getFileExtension(URL url) throws IOException {
        List<String> imageFileTypes = Arrays.asList("jpg", "jpeg", "png", "gif", "bmp", "tiff", "webp", "ico");

        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setConnectTimeout(6000);
            connection.setReadTimeout(6000);
            connection.setRequestMethod("GET");
            connection.connect();
            String contentType = connection.getContentType();

            var fileExtension = contentType.split("/")[1];

            if(!imageFileTypes.contains(fileExtension)) throw new IllegalStateException("Extensão do arquivo não é de uma imagem");
            return fileExtension;
        } catch (Exception e) {
            throw e;
        }
    }
}

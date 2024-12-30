package com.makevideo.make_video.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class FileHandlerService {
    ObjectMapper mapper = new ObjectMapper();

    public <T> T readFile(String uri, Class<T> clazz){
        try {
            Path path = Path.of(uri);
            String content = Files.readString(path);
            return mapper.convertValue(content, clazz);
        } catch (IOException e) {
            throw new RuntimeException("erro ao ler o arquivo no diretorio informado: " + Arrays.toString(e.getStackTrace()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("erro ao realizar o parse de json para texto: " + Arrays.toString(e.getStackTrace()));
        }
    }

    public <T> void writeFile(String uri, T object){
        try {
            Path path = Path.of(uri);
            if(Files.notExists(path)) throw new RuntimeException("caminho do arquivo inexistente: ");

            String fileString = mapper.writeValueAsString(object);
            Files.writeString(path, fileString);
        } catch (IOException e) {
            throw new RuntimeException("erro ao escrever o arquivo no diretorio informado: " + Arrays.toString(e.getStackTrace()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("erro ao realizar o parse de json para texto: " + Arrays.toString(e.getStackTrace()));
        }
    }

    public void createFile(String uri){
        try {
            Path path = Path.of(uri);
            if(Files.exists(path)) throw new RuntimeException("O arquivo ja existe: ");
            Files.createFile(path);
        } catch (IOException e) {
            throw new RuntimeException("erro ao escrever o arquivo no diretorio informado: " + Arrays.toString(e.getStackTrace()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("erro ao realizar o parse de json para texto: " + Arrays.toString(e.getStackTrace()));
        }
    }

    public <T> void createAndWrite(String uri, T object){
        this.createFile(uri);
        this.writeFile(uri, object);
    }
}

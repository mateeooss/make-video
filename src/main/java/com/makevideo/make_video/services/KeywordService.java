package com.makevideo.make_video.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.makevideo.make_video.models.sentences.Sentence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class KeywordService {

    @Value("${apache.tokens}")
    private String tokenModelPath;

    @Value("${apache.pos-tagger}")
    private String posTaggerModelPath;

    @Value("${microservice.pithon.ner.uri}")
    private String pithonMicroServiceUri;

    private final ObjectMapper mapper = new ObjectMapper();

    Logger log = LoggerFactory.getLogger(KeywordService.class);
    RestTemplate restTemplate;

    public KeywordService(){
        restTemplate = new RestTemplate();
    }

    public List<Sentence> getKeyWordsBySentenceList(List<Sentence> sentences) {
        try {
            log.info("Enviando requisição de buscas de keyword");
            var keywords = restTemplate.postForEntity(pithonMicroServiceUri, sentences, String.class).getBody();
            log.info("Requisição feita com sucesso");

            return mapper.readValue(keywords, new TypeReference<List<Sentence>>() {});
        } catch (RestClientException e) {
            throw new RuntimeException("Erro ao solicitar a quebra de keywords do micro serviço python: "+e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

package com.makevideo.make_video.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.makevideo.make_video.models.chatGpt.Request;
import com.makevideo.make_video.models.chatGpt.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class ChatGptService {
    private final String URL = "https://api.openai.com/v1/chat/completions";
    @Value("${openAI.authorization}")
    private String authorization;
    private final ObjectMapper mapper = new ObjectMapper();
    private final Logger log = LoggerFactory.getLogger(ChatGptService.class);

    public Response sendMessage(Request request) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();

            headers.set("Authorization", authorization);
            headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);

            String requestBody = mapper.writeValueAsString(request);
            log.info("Enviado solicitação para o chatgpt:\n"+request);
            RequestEntity<String> requestEntity = RequestEntity
                    .post(URL)
                    .headers(headers)
                    .body(requestBody);

            Response response = restTemplate.exchange(requestEntity, Response.class).getBody();
            log.info("Solicitação realizada com Sucesso: \n"+response);

            return response;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("erro ao realizar o parse de json para texto: "+ e.getMessage());
        } catch (RestClientException e) {
            throw new RuntimeException("erro ao realizar o envio para OpenAi: "+ e.getMessage());
        }
    }
}

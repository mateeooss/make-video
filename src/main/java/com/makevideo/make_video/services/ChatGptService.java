package com.makevideo.make_video.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.makevideo.make_video.models.chatGpt.MessageResp;
import com.makevideo.make_video.models.chatGpt.Request;
import com.makevideo.make_video.models.chatGpt.Response;
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

    private final String AUTHORIZATION;
    private final ObjectMapper mapper = new ObjectMapper();

    public ChatGptService(
            @Value("${openAI.authorization}")
            String authorization
    ) {
        AUTHORIZATION = authorization;
    }

    public Response sendMessage(Request request) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();

            headers.set("Authorization", AUTHORIZATION);
            headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);

            String requestBody = mapper.writeValueAsString(request);
            RequestEntity<String> requestEntity = RequestEntity
                    .post(URL)
                    .headers(headers)
                    .body(requestBody);

            return restTemplate.exchange(requestEntity, Response.class).getBody();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("erro ao realizar o parse de json para texto: "+ e.getMessage());
        } catch (RestClientException e) {
            throw new RuntimeException("erro ao realizar o envio para OpenAi: "+ e.getMessage());
        }
    }
}

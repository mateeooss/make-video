package com.makevideo.make_video.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.makevideo.make_video.models.OpenAiMessage.OpenAiMessageRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChatGptService {
    private final String URL = "https://api.openai.com/v1/chat/completions";

    private final String AUTHORIZATION;

    public ChatGptService(
            @Value("${openAI.authorization}")
            String authorization
    ) {
        AUTHORIZATION = authorization;
    }

    public String sendMessage(OpenAiMessageRequest openAiMessageRequest) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.set("Authorization", AUTHORIZATION);
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper mapper = new ObjectMapper();
        String requestBody =  mapper.writeValueAsString(openAiMessageRequest);
//        String requestBody = "{"
//                + "\"model\": \"gpt-4\","
//                + "\"messages\": ["
//                + "  {\"role\": \"user\", \"content\": \"Hello!\"}"
//                + "]"
//                + "}";
        RequestEntity<String> requestEntity = RequestEntity
                .post(URL)
                .headers(headers)
                .body(requestBody);

        String json = restTemplate.exchange(requestEntity, String.class).getBody();
        return json;
    }
}

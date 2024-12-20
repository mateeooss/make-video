package com.makevideo.make_video.models.openAiMessage;

import lombok.*;

import java.util.List;


@Data
@Getter
@Setter
public class OpenAiMessageRequest {
    private String model;
    private List<OpenAiMessage> messages;

    public OpenAiMessageRequest(String model, List<OpenAiMessage> messages) {
        this.model = model;
        this.messages = messages;
    }

    public OpenAiMessageRequest() {
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<OpenAiMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<OpenAiMessage> messages) {
        this.messages = messages;
    }
}

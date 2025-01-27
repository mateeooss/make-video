package com.makevideo.make_video.models.chatGpt;

import java.util.List;

public class Request {
    private String model = "chatgpt-4o-latest";
    private List<MessageRequest> messages;

    public Request(List<MessageRequest> messages) {
        this.messages = messages;
    }

    public Request() {
    }

    public List<MessageRequest> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageRequest> messages) {
        this.messages = messages;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return "Request{" +
                "model='" + model + '\'' +
                ", messages=" + messages +
                '}';
    }
}

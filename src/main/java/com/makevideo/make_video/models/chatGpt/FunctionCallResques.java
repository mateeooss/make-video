package com.makevideo.make_video.models.chatGpt;

public class FunctionCallResques {
    private String name;
    private String parameters;

    // Getters e Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArguments() {
        return parameters;
    }

    public void setArguments(String arguments) {
        this.parameters = arguments;
    }
}

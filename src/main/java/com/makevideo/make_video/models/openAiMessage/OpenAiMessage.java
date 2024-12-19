package com.makevideo.make_video.models.OpenAiMessage;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class OpenAiMessage {
    private String role;
    private String content;

    public OpenAiMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public OpenAiMessage() {
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

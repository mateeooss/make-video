package com.makevideo.make_video.models.chatGpt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonSerialize
public abstract class AbsMessage {
    private String role;
    private String content;

    @JsonCreator
    public AbsMessage(@JsonProperty("role") String role, @JsonProperty("content") String content){
        this.role = role;
        this.content = content;
    }

    public AbsMessage() {
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

    @Override
    public String toString() {
        return "AbsMessage{" +
                "role='" + role + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}

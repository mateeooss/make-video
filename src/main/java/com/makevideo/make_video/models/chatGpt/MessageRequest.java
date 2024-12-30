package com.makevideo.make_video.models.chatGpt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
public class MessageRequest extends AbsMessage{
    @JsonCreator
    public MessageRequest(@JsonProperty("role") String role, @JsonProperty("content") String content) {
        super(role, content);
    }
}

package com.makevideo.make_video.models.chatGpt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResp extends AbsMessage{
    Object[] tool_calls;
    public MessageResp(@JsonProperty("role") String role, @JsonProperty("content") String content) {
        super(role, content);
    }
}

package com.makevideo.make_video.models.chatGpt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Choice {
    @JsonProperty("index")
    private int index;
    @JsonProperty("message")
    private MessageResp message;
    @JsonProperty("finish_reason")
    private String finishReason;
}

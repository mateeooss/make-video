package com.makevideo.make_video.models.chatGpt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Choice {
    private int index;
    private MessageResp messageResp;
    @JsonProperty("finish_reason")
    private String finishReason;
}

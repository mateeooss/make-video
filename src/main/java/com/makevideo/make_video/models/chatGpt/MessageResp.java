package com.makevideo.make_video.models.chatGpt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResp extends AbsMessage{
    Object[] tool_calls;
    public MessageResp(String role, String content) {
        super(role, content);
    }
}

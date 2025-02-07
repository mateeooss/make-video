package com.makevideo.make_video.models.google;

import lombok.Builder;
import lombok.Data;

@Builder
public record TextToSpeech(
        String text,
        String languageCode,
        String modelName,
        Boolean SSML,
        Double voiceSpeed,
        Double voicePitch
) {
}

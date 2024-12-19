package com.makevideo.make_video.enums;

import lombok.Getter;

@Getter
public enum VideoType {
    VIDEO_WITH_IMAGES("VideoWithImages");

    String className;

    VideoType(String className){
        this.className = className;
    }
}

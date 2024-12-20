package com.makevideo.make_video.enums;

import lombok.Getter;

@Getter
public enum VideoTypeEnum {
    VIDEO_WITH_IMAGES("VideoWithImages");

    String className;

    VideoTypeEnum(String className){
        this.className = className;
    }
}

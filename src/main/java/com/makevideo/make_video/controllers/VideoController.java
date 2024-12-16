package com.makevideo.make_video.controllers;

import com.makevideo.make_video.models.video.VideoTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/video")
public class VideoController {

    @PostMapping()
    public Class<? extends VideoTemplate> createVideo(@RequestBody VideoTemplate teste){
        var a = teste.getClass();
        return a;
    }
}

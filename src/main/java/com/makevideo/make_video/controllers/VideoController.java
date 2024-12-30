package com.makevideo.make_video.controllers;

import com.makevideo.make_video.models.videoTemplate.VideoTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("api/video")
public class VideoController {

    @PostMapping()
    public String createVideo(@RequestBody VideoTemplate videoTemplate) throws IOException {
        videoTemplate.start();
        return "";
    }
}

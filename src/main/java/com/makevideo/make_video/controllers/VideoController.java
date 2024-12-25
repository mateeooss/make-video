package com.makevideo.make_video.controllers;

import com.makevideo.make_video.models.video_template.VideoTemplate;
import com.makevideo.make_video.services.ChatGptService;
import com.makevideo.make_video.services.KeywordExtractorService;
import com.makevideo.make_video.services.SentenceService;
import org.springframework.beans.factory.annotation.Autowired;
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

package com.makevideo.make_video.controllers;

import com.makevideo.make_video.models.video_template.VideoTemplate;
import com.makevideo.make_video.services.ChatGptService;
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

    @Autowired
    private ChatGptService chatGptService;
    @Autowired
    private SentenceService sentenceService;

    @PostMapping()
    public String createVideo(@RequestBody VideoTemplate teste) throws IOException {
//        var openAiMessage = new OpenAiMessage("user", "boa tarde");
//        OpenAiMessageRequest messageRequest = new OpenAiMessageRequest("", Arrays.asList(openAiMessage));
//        return chatGptService.sendMessage(messageRequest);
        sentenceService.processText("O Apache OpenNLP, é uma biblioteca de processamento de linguagem natural. Ele oferece várias ferramentas de NLP.");
        return "";
    }
}

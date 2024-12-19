package com.makevideo.make_video.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.makevideo.make_video.models.OpenAiMessage.OpenAiMessage;
import com.makevideo.make_video.models.OpenAiMessage.OpenAiMessageRequest;
import com.makevideo.make_video.models.video.VideoTemplate;
import com.makevideo.make_video.services.ChatGptService;
import com.makevideo.make_video.services.SentenceService;
import com.makevideo.make_video.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;

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

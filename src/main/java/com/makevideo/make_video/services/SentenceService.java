package com.makevideo.make_video.services;


import com.makevideo.make_video.exception.SentenceModelException;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Service
public class SentenceService {

    @Value("${apache.sentences}")
    private String sentenceModelPath;

    public List<String> getSentences(String text) {
        SentenceDetectorME sentenceDetector = getSentenceDetector();
        String[] sentences = sentenceDetector.sentDetect(text);

        for (String sentence : sentences) {
            System.out.println(sentence);
        }

        return List.of(sentences);
    }

    private SentenceDetectorME getSentenceDetector(){
        try{
            SentenceModel sentenceModel = new SentenceModel(new FileInputStream(sentenceModelPath));
            return new SentenceDetectorME(sentenceModel);
        } catch(IOException e){
            throw new SentenceModelException("Erro ao carregar o modelo de Sentença em: " + sentenceModelPath, e);
        }

    }
}

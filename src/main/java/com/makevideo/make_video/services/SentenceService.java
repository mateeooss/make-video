package com.makevideo.make_video.services;


import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

@Service
public class SentenceService {
    public void processText(String text) throws IOException {
        // Carregar o modelo de detecção de sentenças em português
        SentenceModel sentenceModel = new SentenceModel(new FileInputStream("src/main/resources/setence-model/opennlp-pt-ud-gsd-sentence-1.2-2.5.0.bin"));
        SentenceDetectorME sentenceDetector = new SentenceDetectorME(sentenceModel);

        // Detectar as sentenças
        String[] sentences = sentenceDetector.sentDetect(text);

        // Exibir as sentenças detectadas
        System.out.println("Sentenças detectadas:");
        for (String sentence : sentences) {
            System.out.println(sentence);
        }
    }
}

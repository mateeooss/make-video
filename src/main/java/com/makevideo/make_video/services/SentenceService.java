package com.makevideo.make_video.services;


import com.makevideo.make_video.exception.SentenceModelException;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class SentenceService {

    @Value("${apache.sentences}")
    private String sentenceModelPath;
    Logger log = LoggerFactory.getLogger(SentenceService.class);

    public List<String> getSentences(String text) {
        if(text == null) throw new IllegalStateException("O texto fornecido para extrair sentenças está null");

        log.info("Iniciando busca de sentenças");
        SentenceDetectorME sentenceDetector = getSentenceDetector();
        String[] sentences = sentenceDetector.sentDetect(text);
        log.info("Busca de sentenças realizada com sucesso");
        return List.of(sentences);
    }

    private SentenceDetectorME getSentenceDetector(){
        try (InputStream file = new FileInputStream(sentenceModelPath)){
            SentenceModel sentenceModel = new SentenceModel(file);
            return new SentenceDetectorME(sentenceModel);
        } catch(IOException e){
            throw new SentenceModelException("Erro ao carregar o modelo de Sentença em: " + sentenceModelPath, e);
        }

    }
}

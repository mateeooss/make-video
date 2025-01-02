package com.makevideo.make_video.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;

@Service
public class KeywordService {

    @Value("${apache.tokens}")
    private String tokenModelPath;

    @Value("${apache.pos-tagger}")
    private String posTaggerModelPath;

    @Value("${microservice.pithon.ner.uri}")
    private String pithonMicroServiceUri;

    RestTemplate restTemplate;

    public KeywordService(){
        restTemplate = new RestTemplate();
    }

    public void getKeyWords(String text) {
        var a = Arrays.asList(text, "aaaaaa");
        String encodedText;
        try {
            encodedText = URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        ResponseEntity<String> response = restTemplate.postForEntity(pithonMicroServiceUri, a, String.class);

        System.out.println("Resposta: " + response.getBody());
    }

//    public void getKeyWords(String text) {
//        TokenizerModel model = getTokenizationModel();
//        Tokenizer tokenizer = new TokenizerME(model);
//        String[] tokens = tokenizer.tokenize(text);
//
//        POSTaggerME posTagger = getPosTaggerModel();
//
//        // Obter as tags de POS
//        String[] tags = posTagger.tag(tokens);
//        posTagger.topKSequences()
//        // Filtrar apenas substantivos
//        List<String> keywords = new ArrayList<>();
//        for (int i = 0; i < tokens.length; i++) {
//            if (tags[i].equals("NOUN") || tags[i].equals("PROPN") || tags[i].equals("ADJ")) {
//                keywords.add(tokens[i]);
//            }
//        }
//
//        // Exibir as keywords extraídas
//        System.out.println("Keywords extraídas:");
//        for (String keyword : keywords) {
//            System.out.println(keyword);
//        }
//    }

//    private TokenizerModel getTokenizationModel(){
//        try {
//            InputStream modelIn = new FileInputStream(tokenModelPath);
//            return new TokenizerModel(modelIn);
//        } catch (NullPointerException | IOException e) {
//            throw new TokenizationModelException("Erro ao carregar o modelo de tokenização em: " + tokenModelPath, e);
//        }
//    }
//
//    private POSTaggerME getPosTaggerModel(){
//        POSModel posModel = null;
//        try {
//            posModel = new POSModel(new FileInputStream(posTaggerModelPath));
//            return new POSTaggerME(posModel);
//        } catch (NullPointerException | IOException e) {
//            throw new PosTaggerModelException("Erro ao carregar o modelo de tokenização em: " + posTaggerModelPath, e);
//        }
//    }
}

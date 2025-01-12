package com.makevideo.make_video.models.sentences;

import java.util.List;

public class Sentence {
    private String text;
    private List<String> keyWords;
    private String imagesUrl;

    public Sentence(String text, List<String> keyWords, String imagesUrl) {
        this.text = text;
        this.keyWords = keyWords;
        this.imagesUrl = imagesUrl;
    }

    public Sentence() {
    }

    public Sentence(String text){
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(List<String> keyWords) {
        this.keyWords = keyWords;
    }

    public String getImagesUrl() {
        return imagesUrl;
    }

    public void setImagesUrl(String imagesUrl) {
        this.imagesUrl = imagesUrl;
    }
}

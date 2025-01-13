package com.makevideo.make_video.models.sentences;

import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.ArrayList;
import java.util.List;

public class Sentence {
    private String text;
    private List<String> keyWords;
    private List<String> imagesUrl = new ArrayList<String>();

    public Sentence(String text, List<String> keyWords, List<String> imagesUrl) {
        this.text = text;
        this.keyWords = keyWords;
        this.imagesUrl = imagesUrl == null ? new ArrayList<String>() : imagesUrl;
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

    public List<String> getImagesUrl() {
        return imagesUrl;
    }

//    @JsonSetter("imagesUrl")
    public void setImagesUrl(List<String> imagesUrl) {
        this.imagesUrl = (imagesUrl == null) ? new ArrayList<>() : imagesUrl;
    }
}

package com.makevideo.make_video.models.sentences;

import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.ArrayList;
import java.util.List;

public class Sentence {
    private String text;
    private String imagesPath;
    private String rootPath;
    private String audioPath;
    private String videoPath;
    private List<String> keyWords;
    private List<String> imagesUrl = new ArrayList<String>();

    public Sentence(String text, List<String> keyWords, List<String> imagesUrl) {
        this.text = text;
        this.keyWords = keyWords;
        this.imagesUrl = imagesUrl == null ? new ArrayList<String>() : imagesUrl;
    }

    public Sentence() {
    }

    public Sentence(String text, String rootPath, String imagesPath, String audioPath, String videoPath) {
        this.text = text;
        this.rootPath = rootPath;
        this.imagesPath = imagesPath;
        this.audioPath = audioPath;
        this.videoPath = videoPath;
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

    public String getImagesPath() {
        return imagesPath;
    }

    public void setImagesPath(String imagesPath) {
        this.imagesPath = imagesPath;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }
}

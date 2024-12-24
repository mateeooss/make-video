package com.makevideo.make_video.exception;

public class SentenceModelException extends RuntimeException{
    public SentenceModelException(String message) {
        super(message);
    }

    public SentenceModelException(String message, Throwable cause) {
        super(message, cause);
    }
}

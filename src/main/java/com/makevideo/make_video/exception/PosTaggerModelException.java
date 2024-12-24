package com.makevideo.make_video.exception;

public class PosTaggerModelException extends RuntimeException{
    public PosTaggerModelException(String message) {
        super(message);
    }

    public PosTaggerModelException(String message, Throwable cause) {
        super(message, cause);
    }
}

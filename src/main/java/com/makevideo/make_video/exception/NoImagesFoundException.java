package com.makevideo.make_video.exception;

public class NoImagesFoundException extends RuntimeException{
    public NoImagesFoundException(String message) {
        super(message);
    }

    public NoImagesFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

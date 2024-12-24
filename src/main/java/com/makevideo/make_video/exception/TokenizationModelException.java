package com.makevideo.make_video.exception;

public class TokenizationModelException extends RuntimeException{
    public TokenizationModelException(String message) {
        super(message);
    }

    public TokenizationModelException(String message, Throwable cause) {
        super(message, cause);
    }
}

package com.urlKatz.LambdaService.LambdaException;

public class UrlNotFoundException extends RuntimeException{

    public UrlNotFoundException(String message) {
        super(message);
    }
}

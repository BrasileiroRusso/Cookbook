package ru.geekbrains.cookbook.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(){
        super();
    }

    public ResourceNotFoundException(String errorMessage){
        super(errorMessage);
    }

    public ResourceNotFoundException(String errorMessage, Exception cause){
        super(errorMessage, cause);
    }
}

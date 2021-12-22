package ru.geekbrains.cookbook.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceCannotDeleteException extends RuntimeException {
    public ResourceCannotDeleteException(){
        super();
    }

    public ResourceCannotDeleteException(String message){
        super(message);
    }
}

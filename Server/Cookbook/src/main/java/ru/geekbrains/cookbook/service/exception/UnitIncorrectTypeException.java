package ru.geekbrains.cookbook.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UnitIncorrectTypeException extends RuntimeException {
    public UnitIncorrectTypeException(){
        super();
    }

    public UnitIncorrectTypeException(String errorMessage){
        super(errorMessage);
    }

    public UnitIncorrectTypeException(String errorMessage, Exception cause){
        super(errorMessage, cause);
    }
}

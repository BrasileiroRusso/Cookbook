package ru.geekbrains.cookbook.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class MainUnitNotDefined extends RuntimeException {
    public MainUnitNotDefined(){
        super();
    }

    public MainUnitNotDefined(String errorMessage){
        super(errorMessage);
    }

    public MainUnitNotDefined(String errorMessage, Exception cause){
        super(errorMessage, cause);
    }
}

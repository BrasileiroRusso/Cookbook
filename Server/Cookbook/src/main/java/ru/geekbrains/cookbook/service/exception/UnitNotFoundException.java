package ru.geekbrains.cookbook.service.exception;

public class UnitNotFoundException extends RuntimeException {
    public UnitNotFoundException(){
        super();
    }

    public UnitNotFoundException(String message){
        super(message);
    }

    public UnitNotFoundException(Exception cause){
        super(cause);
    }
}

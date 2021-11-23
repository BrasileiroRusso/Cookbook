package ru.geekbrains.cookbook.service.exception;

public class UnitCannotDeleteException extends RuntimeException {
    public UnitCannotDeleteException(){
        super();
    }

    public UnitCannotDeleteException(String message){
        super(message);
    }
}

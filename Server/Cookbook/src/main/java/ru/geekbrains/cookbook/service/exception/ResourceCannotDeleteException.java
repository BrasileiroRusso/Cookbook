package ru.geekbrains.cookbook.service.exception;

public class ResourceCannotDeleteException extends RuntimeException {
    public ResourceCannotDeleteException(){
        super();
    }

    public ResourceCannotDeleteException(String message){
        super(message);
    }
}

package ru.geekbrains.cookbook.service.exception;

public class CategoryCannotDeleteException extends RuntimeException {
    public CategoryCannotDeleteException(){
        super();
    }

    public CategoryCannotDeleteException(String message){
        super(message);
    }
}

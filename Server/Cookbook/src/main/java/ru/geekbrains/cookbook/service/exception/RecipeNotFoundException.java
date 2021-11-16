package ru.geekbrains.cookbook.service.exception;

public class RecipeNotFoundException extends RuntimeException {
    public RecipeNotFoundException(){
        super();
    }

    public RecipeNotFoundException(String message){
        super(message);
    }

    public RecipeNotFoundException(Exception cause){
        super(cause);
    }
}

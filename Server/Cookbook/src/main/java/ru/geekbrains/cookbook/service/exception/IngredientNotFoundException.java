package ru.geekbrains.cookbook.service.exception;

public class IngredientNotFoundException extends RuntimeException {
    public IngredientNotFoundException(){
        super();
    }

    public IngredientNotFoundException(String message){
        super(message);
    }

    public IngredientNotFoundException(Exception cause){
        super(cause);
    }
}

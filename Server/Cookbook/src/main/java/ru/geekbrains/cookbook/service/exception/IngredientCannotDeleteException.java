package ru.geekbrains.cookbook.service.exception;

public class IngredientCannotDeleteException extends RuntimeException {
    public IngredientCannotDeleteException(){
        super();
    }

    public IngredientCannotDeleteException(String message){
        super(message);
    }
}

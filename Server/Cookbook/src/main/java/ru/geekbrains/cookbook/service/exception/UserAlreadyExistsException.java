package ru.geekbrains.cookbook.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyExistsException extends RuntimeException {
    private String username;

    public UserAlreadyExistsException(String username){
        super("Пользователь с именем " + username + " уже существует.");
    }
}

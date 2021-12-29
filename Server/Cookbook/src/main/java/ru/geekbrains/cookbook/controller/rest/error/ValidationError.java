package ru.geekbrains.cookbook.controller.rest.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.FieldError;

@Data
@AllArgsConstructor
public class ValidationError {
    private String code;
    private String message;

    public static ValidationError createFromFieldError(FieldError fieldError){
        return new ValidationError(fieldError.getCode(), fieldError.getDefaultMessage());
    }
}

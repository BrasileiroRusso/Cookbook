package ru.geekbrains.cookbook.validation;

import ru.geekbrains.cookbook.dto.UserDto;
import ru.geekbrains.cookbook.validation.constraint.PasswordMatches;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context){
        UserDto user = (UserDto) obj;
        return user.getPassword().equals(user.getMatchingPassword());
    }
}

package ru.geekbrains.cookbook.validation;

import ru.geekbrains.cookbook.dto.UserRatingDto;
import ru.geekbrains.cookbook.validation.constraint.ArgumentedRating;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ArgumentedRatingValidator implements ConstraintValidator<ArgumentedRating, Object> {
    private static final int MIN_NON_ARGUMENTED_RATING = 4;

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context){
        UserRatingDto rating = (UserRatingDto) obj;
        return rating.getRate() >= MIN_NON_ARGUMENTED_RATING || (rating.getComment() != null && !rating.getComment().isEmpty());
    }
}

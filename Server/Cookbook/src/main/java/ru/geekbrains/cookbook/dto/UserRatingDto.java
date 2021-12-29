package ru.geekbrains.cookbook.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.geekbrains.cookbook.validation.constraint.ArgumentedRating;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
//@ArgumentedRating
public class UserRatingDto {
    @Min(value = 1)
    @Max(value = 5)
    private int rate;
    private String comment;
}

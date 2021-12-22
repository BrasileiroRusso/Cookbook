package ru.geekbrains.cookbook.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
public class UserRatingDto {
    @Min(value = 1)
    @Max(value = 5)
    private int rate;
}

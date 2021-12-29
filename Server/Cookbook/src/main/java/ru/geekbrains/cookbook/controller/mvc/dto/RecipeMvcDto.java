package ru.geekbrains.cookbook.controller.mvc.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@Data
@EqualsAndHashCode(exclude = "ingredients")
public class RecipeMvcDto {
    private Long id;
    private Long categoryId;
    private String title;
    private String description;
    private List<String> images;
    private List<RecipeIngredient> ingredients;

    @Data
    @NoArgsConstructor
    public static class RecipeIngredient {
        private Long ingredientId;
        private Long unitId;
        private BigDecimal amount;;
    }
}

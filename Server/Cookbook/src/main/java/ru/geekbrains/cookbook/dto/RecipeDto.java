package ru.geekbrains.cookbook.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import ru.geekbrains.cookbook.auth.User;
import ru.geekbrains.cookbook.domain.Category;
import ru.geekbrains.cookbook.domain.HashTag;
import ru.geekbrains.cookbook.domain.Recipe;
import ru.geekbrains.cookbook.domain.RecipeIngredient;
import ru.geekbrains.cookbook.domain.file.LinkedFiles;

import javax.persistence.Column;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false)
public class RecipeDto extends RepresentationModel<RecipeDto> {
    private Long id;
    private Category category;
    private String title;
    private String description;
    private User user;
    private String imagePath;
    private Set<RecipeIngredient> ingredients = new HashSet<>();
    private List<Recipe.RecipeStep> steps = new ArrayList<>();
    private RecipeRatingDto rating;
    private Set<HashTag> tags;
    private Integer prepareTime;
    private String comment;
    @JsonIgnore
    private List<Image> images;

    @Data
    @NoArgsConstructor
    public static class Image{
        private String fileKey;
        private String fileUri;
        private String resourcePart;
    }
}

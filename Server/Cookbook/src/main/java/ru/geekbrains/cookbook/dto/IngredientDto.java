package ru.geekbrains.cookbook.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper=false)
public class IngredientDto extends RepresentationModel<IngredientDto> {
    private Long id;
    @NotNull
    @NotEmpty
    private String briefName;
    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    private Boolean group;
    private Long parentId;
    private String parentBrief;
    private String imagePath;
    private Set<SubIngredient> subIngredients;

    @Data
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper=false)
    public static class SubIngredient extends RepresentationModel<SubIngredient> {
        private Long id;
        private String title;
    }
}

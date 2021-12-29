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
public class CategoryDto extends RepresentationModel<CategoryDto> {
    private Long id;
    @NotNull
    @NotEmpty
    private String title;
    private Long parentId;
    private String parentTitle;
    private Set<SubCategory> subCategories;

    @Data
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper=false)
    public static class SubCategory extends RepresentationModel<SubCategory> {
        private Long id;
        private String title;
    }
}

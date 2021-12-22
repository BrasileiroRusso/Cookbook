package ru.geekbrains.cookbook.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import java.util.Set;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryDto extends RepresentationModel<CategoryDto> {
    private Long id;
    private String title;
    private Long parentId;
    private String parentTitle;
    private Set<SubCategory> subCategories;

    @Data
    @AllArgsConstructor
    public static class SubCategory extends RepresentationModel<SubCategory> {
        private Long id;
        private String title;
    }
}

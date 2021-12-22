package ru.geekbrains.cookbook.mapper;

import ru.geekbrains.cookbook.controller.rest.CategoryController;
import ru.geekbrains.cookbook.domain.Category;
import ru.geekbrains.cookbook.dto.CategoryDto;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class CategoryMapper {
    public static CategoryDto categoryToDto(Category category){
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setTitle(category.getTitle());
        if(category.getParentCategory() != null){
            categoryDto.setParentId(category.getParentCategory().getId());
            categoryDto.setParentTitle(category.getParentCategory().getTitle());
        }
        if(category.getChildCategorySet() != null){
            Set<CategoryDto.SubCategory> subCategories = category.getChildCategorySet()
                    .stream()
                    .map(CategoryMapper::toSubCategory)
                    .collect(Collectors.toSet());
            subCategories.forEach(c -> c.add(linkTo(methodOn(CategoryController.class).getCategoryByID(c.getId())).withSelfRel()));
            categoryDto.setSubCategories(subCategories);
        }
        categoryDto.add(linkTo(methodOn(CategoryController.class).getCategoryByID(categoryDto.getId()))
                .withSelfRel());
        if(categoryDto.getParentId() != null)
            categoryDto.add(linkTo(methodOn(CategoryController.class).getCategoryByID(categoryDto.getParentId()))
                    .withRel("parent"));
        return categoryDto;
    }

    public static List<CategoryDto> categoryListToDtoList(List<Category> categoryList){
        if(categoryList == null)
            return null;
        return categoryList.stream().map(CategoryMapper::categoryToDto).collect(Collectors.toList());
    }

    private static CategoryDto.SubCategory toSubCategory(Category category){
        return new CategoryDto.SubCategory(category.getId(), category.getTitle());
    }
}

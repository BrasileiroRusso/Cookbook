package ru.geekbrains.cookbook.mapper;

import ru.geekbrains.cookbook.controller.rest.IngredientController;
import ru.geekbrains.cookbook.domain.Ingredient;
import ru.geekbrains.cookbook.dto.IngredientDto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class IngredientMapper {
    public static IngredientDto ingredientToDto(Ingredient ingredient){
        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setId(ingredient.getId());
        ingredientDto.setBriefName(ingredient.getBriefName());
        ingredientDto.setName(ingredient.getName());
        ingredientDto.setGroup(ingredient.getGroup());
        if(ingredient.getParentIngredient() != null){
            ingredientDto.setParentId(ingredient.getParentIngredient().getId());
            ingredientDto.setParentBrief(ingredient.getParentIngredient().getBriefName());
        }
        if(ingredient.getChildIngredients() != null){
            Set<IngredientDto.SubIngredient> subIngredients = ingredient.getChildIngredients()
                    .stream()
                    .map(IngredientMapper::toSubIngredient)
                    .collect(Collectors.toSet());
            subIngredients.forEach(c -> c.add(linkTo(methodOn(IngredientController.class).getIngredientByID(c.getId())).withSelfRel()));
            ingredientDto.setSubIngredients(subIngredients);
        }
        ingredientDto.add(linkTo(methodOn(IngredientController.class).getIngredientByID(ingredientDto.getId()))
                .withSelfRel());
        if(ingredientDto.getParentId() != null)
            ingredientDto.add(linkTo(methodOn(IngredientController.class).getIngredientByID(ingredientDto.getParentId()))
                    .withRel("parent"));
        return ingredientDto;
    }

    public static List<IngredientDto> ingredientListToDtoList(List<Ingredient> ingredientList){
        if(ingredientList == null)
            return null;
        return ingredientList.stream().map(IngredientMapper::ingredientToDto).collect(Collectors.toList());
    }

    private static IngredientDto.SubIngredient toSubIngredient(Ingredient ingredient){
        return new IngredientDto.SubIngredient(ingredient.getId(), ingredient.getBriefName());
    }
}

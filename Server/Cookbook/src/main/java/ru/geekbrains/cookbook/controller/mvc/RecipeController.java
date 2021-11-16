package ru.geekbrains.cookbook.controller.mvc;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.cookbook.domain.Recipe;
import ru.geekbrains.cookbook.service.CategoryService;
import ru.geekbrains.cookbook.service.RecipeService;
import javax.validation.Validator;
import java.util.List;

@Controller("mvcRecipeController")
@RequestMapping("/recipes")
@AllArgsConstructor
public class RecipeController {
    private final RecipeService recipeService;
    private final CategoryService categoryService;
    private final Validator validator;

    @GetMapping
    public String getProductList(Model model){
        List<Recipe> recipes = recipeService.findAll();
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("recipes", recipes);
        return "recipe/list";
    }

}


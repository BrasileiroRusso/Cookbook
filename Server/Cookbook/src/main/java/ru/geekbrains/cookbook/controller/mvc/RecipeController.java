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
    public String getRecipeList(Model model){
        List<Recipe> recipes = recipeService.findAll();
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("recipes", recipes);
        return "recipe/list";
    }

    @GetMapping("/recipe/{recipe_id}")
    public String getRecipeById(@PathVariable(value="recipe_id") Long id, Model model){
        Recipe recipe = recipeService.getRecipeById(id);
        model.addAttribute("recipe", recipe);
        return "recipe/recipe_page";
    }

    @GetMapping("/search")
    public String searchRecipeByName(@RequestParam(value = "title", required = true) String title, Model model) {
        /*List<Recipe> recipes = recipeService.findByTitleStartingWith(title);*/
//        model.addAttribute("recipes", recipes);
        model.addAttribute("categories", categoryService.findAll());
        return "recipe/list";
    }

    @GetMapping("/add_recipe")
    public String addRecipe(@RequestParam(value="recipe_id", required = false) Long id, Model model) {
        if(id !=null) {
            Recipe recipe = recipeService.getRecipeById(id);
            model.addAttribute("recipe", recipe);
        } else {
            model.addAttribute("recipe", new Recipe());
        }
        model.addAttribute("categories", categoryService.findAll());
        return "recipe/add_recipe";
    }

    @PostMapping("/add_recipe")
        public String createNewRecipe(@ModelAttribute("recipe") Recipe recipe) {
        recipeService.saveRecipe(recipe);
        return "redirect:/recipes";
    }

    @GetMapping("/remove")
    public String removeRecipeById(@RequestParam(value = "recipe_id") Long id) {
        recipeService.removeRecipe(id);

        return "redirect:/recipes";
    }
}


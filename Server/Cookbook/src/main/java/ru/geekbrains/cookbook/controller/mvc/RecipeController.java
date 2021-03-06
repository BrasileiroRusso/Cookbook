package ru.geekbrains.cookbook.controller.mvc;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.geekbrains.cookbook.domain.Recipe;
import ru.geekbrains.cookbook.domain.file.LinkedFiles;
import ru.geekbrains.cookbook.dto.RecipeDto;
import ru.geekbrains.cookbook.service.CategoryService;
import ru.geekbrains.cookbook.service.RecipeService;
import ru.geekbrains.cookbook.service.UploadFileService;
import ru.geekbrains.cookbook.service.UserService;
import javax.validation.Validator;

@Controller("mvcRecipeController")
@RequestMapping("/recipes")
@AllArgsConstructor
public class RecipeController {
    private final RecipeService recipeService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final UploadFileService uploadFileService;
    private final Validator validator;

    @GetMapping
    public String getRecipeList(Model model,
                                Pageable pageable,
                                @RequestParam(name="categoryId", required = false) Long categoryId,
                                @RequestParam(name="title", required = false) String titleRegex){
        Page<RecipeDto> recipesPage = recipeService.findAll(pageable, categoryId, titleRegex, null, null);
        recipesPage.getContent().forEach(r -> r.setImagePath(FileController.getFileUrl(r.getImagePath())));
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("recipes", recipesPage.getContent());
        model.addAttribute("page", recipesPage);
        return "recipe/list";
    }

    @GetMapping("/recipe/{recipe_id}")
    public String getRecipeById(@PathVariable(value="recipe_id") Long id, Model model){
        RecipeDto recipe = recipeService.getRecipeById(id);
        LinkedFiles linkedFiles = uploadFileService.getUploadedFileListByResource(id, Recipe.class);
        linkedFiles = FileController.transformUriInLinkedFiles(linkedFiles);
        model.addAttribute("recipe", recipe);
        model.addAttribute("linkedFiles", linkedFiles);
        return "recipe/recipe";
    }

    @GetMapping("/add_recipe")
    public String addRecipe(@RequestParam(value="recipe_id", required = false) Long id, Model model) {
        if(id != null) {
            RecipeDto recipe = recipeService.getRecipeById(id);
            model.addAttribute("recipe", recipe);
        } else {
            model.addAttribute("recipe", new RecipeDto());
        }
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("users", userService.getUserList());
        model.addAttribute("quote", "'");
        return "recipe/add_recipe";
    }

    @PostMapping("/add_recipe")
    public String createNewRecipe(@ModelAttribute("recipe") RecipeDto recipe, MultipartFile image) {
        System.out.println("UserID = " + recipe.getUser().getId());
        recipeService.saveRecipe(recipe);
        return "redirect:/recipes";
    }

    @GetMapping("/remove")
    public String removeRecipeById(@RequestParam(value = "recipe_id") Long id) {
        recipeService.removeRecipe(id);

        return "redirect:/recipes";
    }
}

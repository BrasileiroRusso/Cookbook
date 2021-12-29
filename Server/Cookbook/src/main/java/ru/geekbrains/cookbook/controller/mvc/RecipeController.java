package ru.geekbrains.cookbook.controller.mvc;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.cookbook.controller.mvc.dto.RecipeMvcDto;
import ru.geekbrains.cookbook.domain.Recipe;
import ru.geekbrains.cookbook.domain.file.LinkedFiles;
import ru.geekbrains.cookbook.dto.RecipeDto;
import ru.geekbrains.cookbook.mapper.RecipeMapper;
import ru.geekbrains.cookbook.service.*;

import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;

@Controller("mvcRecipeController")
@RequestMapping("/recipes")
@AllArgsConstructor
public class RecipeController {
    private final RecipeService recipeService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final UploadFileService uploadFileService;
    private final IngredientService ingredientService;
    private final UnitService unitService;
    private final Validator validator;

    @GetMapping
    public String getRecipeList(Model model,
                                Pageable pageable,
                                @RequestParam(name="categoryId", required = false) Long categoryId,
                                @RequestParam(name="title", required = false) String titleRegex,
                                Authentication authentication){
        Page<RecipeDto> recipesPage = recipeService.findAll(pageable, categoryId, titleRegex, null, null, null);
        recipesPage.getContent().forEach(r -> r.setImagePath(FileController.getFileUrl(r.getImagePath())));
        String username = authentication.getName();
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("recipes", recipesPage.getContent());
        model.addAttribute("page", recipesPage);
        model.addAttribute("username", username);
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

    @GetMapping("/edit_recipe")
    public String addRecipe(@RequestParam(value="recipe_id", required = false) Long id, Model model) {
        if(id != null) {
            RecipeDto recipe = recipeService.getRecipeById(id);
            LinkedFiles linkedFiles = uploadFileService.getUploadedFileListByResource(id, Recipe.class);
            List<RecipeDto.Image> images = RecipeMapper.linkedFilesToImageList(linkedFiles);
            System.out.println("Images: " + images);
            images.forEach(image -> image.setFileUri(FileController.getFileUrl(image.getFileKey())));
            recipe.setImages(images);
            model.addAttribute("recipe", recipe);
        } else {
            RecipeDto recipe =  new RecipeDto();
            List<RecipeDto.Image> images = new ArrayList<>();
            recipe.setImages(images);
            model.addAttribute("recipe", new RecipeDto());
        }
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("users", userService.getUserList());
        model.addAttribute("ingredients", ingredientService.findAll());
        model.addAttribute("units", unitService.findAll());
        return "recipe/edit_recipe";
    }

    @PostMapping(path="/edit_recipe")
    @ResponseBody
    public ResponseEntity<?> createNewRecipe(@RequestBody RecipeMvcDto recipe) {
        System.out.println("RecipeMvc: " + recipe);
        //recipeService.saveRecipe(recipe);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/remove")
    public String removeRecipeById(@RequestParam(value = "recipe_id") Long id) {
        recipeService.removeRecipe(id);

        return "redirect:/recipes";
    }
}

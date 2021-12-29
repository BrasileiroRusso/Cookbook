package ru.geekbrains.cookbook.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.cookbook.auth.User;
import ru.geekbrains.cookbook.dto.RecipeDto;
import ru.geekbrains.cookbook.service.UserService;
import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user")
@Tag(name = "Пользователи", description = "API для пользователей")
public class UserController {
    private UserService userService;

    @Operation(summary = "Возвращает список пользователей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = User.class)))}
            )}
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<User> getAllUsers(){
        return userService.getUserList();
    }

    @Operation(summary = "Возвращает пользователя по идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Указан некорректный идентификатор пользователя"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{user_id}")
    public User getUserByID(@Parameter(description = "Идентификатор пользователя", required = true) @PathVariable(value="user_id") Long userId) {
        return userService.getUser(userId);
    }

    @Operation(summary = "Возвращает список избранных рецептов пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {
                            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = RecipeDto.class)))
                    }),
            @ApiResponse(responseCode = "400", description = "Указан некорректный идентификатор пользователя"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{user_id}/favorite")
    public ResponseEntity<?> getFavoriteRecipes(@Parameter(description = "Идентификатор пользователя", required = true) @PathVariable(value="user_id") Long userId){
        Set<RecipeDto> favoriteRecipes = userService.getFavoriteRecipes(userId);
        favoriteRecipes.forEach(r -> r.setImagePath(FileController.getFileUrl(r.getImagePath())));
        return ResponseEntity.ok(userService.getFavoriteRecipes(userId));
    }

    @Operation(summary = "Добавляет рецепт в избранное пользователя", description = "Добавляет рецепт с заданным идентификатором в избранное пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно"),
            @ApiResponse(responseCode = "400", description = "Указан некорректный идентификатор пользователя или рецепта"),
            @ApiResponse(responseCode = "404", description = "Пользователь или рецепт не найден")
    })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{user_id}/favorite/{recipe_id}")
    public ResponseEntity<?> addRecipeToFavorites(@Parameter(description = "Идентификатор пользователя", required = true) @PathVariable(value="user_id") Long userId,
                                                  @Parameter(description = "Идентификатор рецепта", required = true) @PathVariable(value="recipe_id") Long recipeId){
        userService.addRecipeToFavorites(userId, recipeId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удаляет рецепт из избранного пользователя", description = "Удаляет рецепт с заданным идентификатором из избранного пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно"),
            @ApiResponse(responseCode = "400", description = "Указан некорректный идентификатор пользователя или рецепта"),
            @ApiResponse(responseCode = "404", description = "Пользователь или рецепт не найден")
    })
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{user_id}/favorite/{recipe_id}")
    public ResponseEntity<?> removeRecipeFromFavorites(@Parameter(description = "Идентификатор пользователя", required = true) @PathVariable(value="user_id") Long userId,
                                                       @Parameter(description = "Идентификатор рецепта", required = true) @PathVariable(value="recipe_id") Long recipeId){
        userService.removeRecipeFromFavorites(userId, recipeId);
        return ResponseEntity.ok().build();
    }

}

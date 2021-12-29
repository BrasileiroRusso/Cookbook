package ru.geekbrains.cookbook.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.geekbrains.cookbook.auth.User;
import ru.geekbrains.cookbook.domain.HashTag;
import ru.geekbrains.cookbook.domain.Recipe;
import ru.geekbrains.cookbook.domain.UserRating;
import ru.geekbrains.cookbook.domain.file.LinkedFiles;
import ru.geekbrains.cookbook.domain.file.UploadedFileLink;
import ru.geekbrains.cookbook.dto.FileLinkDto;
import ru.geekbrains.cookbook.dto.RecipeDto;
import ru.geekbrains.cookbook.dto.UploadedFileLinkDto;
import ru.geekbrains.cookbook.dto.UserRatingDto;
import ru.geekbrains.cookbook.event.RatingChangedEvent;
import ru.geekbrains.cookbook.event.RegistrationCompletedEvent;
import ru.geekbrains.cookbook.service.RatingService;
import ru.geekbrains.cookbook.service.RecipeService;
import ru.geekbrains.cookbook.service.UploadFileService;
import ru.geekbrains.cookbook.service.impl.UserServiceImpl;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/recipe")
@Tag(name = "Рецепты", description = "API для рецептов")
public class RecipeController {
    private RecipeService recipeService;
    private UploadFileService uploadFileService;
    private RatingService ratingService;
    private UserServiceImpl userService;
    private ApplicationEventPublisher eventPublisher;

    @Operation(summary = "Возвращает список рецептов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                         content = { @Content(mediaType = "application/json",
                                              array = @ArraySchema(schema = @Schema(implementation = RecipeDto.class)))}
                        )}
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Page<RecipeDto> getAllRecipes(@Parameter(description = "Параметры пагинации и сортировки", required = false) Pageable pageable,
                                         @Parameter(description = "Идентификатор категории", required = false) @RequestParam(value = "categoryId", required = false) Long categoryId,
                                         @Parameter(description = "Фрагмент наименования блюда", required = false) @RequestParam(value = "name", required = false) String name,
                                         @Parameter(description = "Максимальное время приготовления", required = false) @RequestParam(value = "prepareTime", required = false) Integer prepareTime,
                                         @Parameter(description = "Список тэгов рецепта (через запятую)", required = false) @RequestParam(value = "tags", required = false) List<String> tags,
                                         @Parameter(description = "Идентификатор автора рецепта", required = false) @RequestParam(value = "author", required = false) Long authorId) {
        Page<RecipeDto> recipePage = recipeService.findAll(pageable, categoryId, name, prepareTime, tags, authorId);
        recipePage.getContent().forEach(r -> r.setImagePath(FileController.getFileUrl(r.getImagePath())));
        return recipePage;
    }

    @Operation(summary = "Возвращает рецепт по идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                         content = {
                                    @Content(mediaType = "application/json", schema = @Schema(implementation = RecipeDto.class))
                         }),
            @ApiResponse(responseCode = "400", description = "Указан некорректный идентификатор"),
            @ApiResponse(responseCode = "404", description = "Рецепт не найден")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{recipe_id}")
    public ResponseEntity<?> getRecipeByID(@Parameter(description = "Идентификатор рецепта", required = true) @PathVariable(value="recipe_id") Long recipeId){
        RecipeDto recipeDto = recipeService.getRecipeById(recipeId);
        recipeDto.setImagePath(FileController.getFileUrl(recipeDto.getImagePath()));
        return ResponseEntity.ok(recipeDto);
    }

    @Operation(summary = "Создает новый рецепт", description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = RecipeDto.class))
                    }),
            @ApiResponse(responseCode = "405", description = "Некорректные входные данные")
    })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    public ResponseEntity<?> addRecipe(@Parameter(description = "Новый рецепт", required = true) @RequestBody RecipeDto recipeDto) {
        recipeDto.setId(null);
        recipeDto = recipeService.saveRecipe(recipeDto);
        return ResponseEntity.ok(recipeDto);
    }

    @Operation(summary = "Обновляет существующий рецепт", description = "Обновляет существующий рецепт с заданным идентификатором")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно"),
            @ApiResponse(responseCode = "400", description = "Указан некорректный идентификатор"),
            @ApiResponse(responseCode = "404", description = "Рецепт не найден"),
            @ApiResponse(responseCode = "405", description = "Некорректные входные данные")
    })
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(path = "/{recipe_id}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> updateRecipe(@Parameter(description = "Идентификатор рецепта", required = true) @PathVariable(value="recipe_id") Long recipeId,
                                          @Parameter(description = "Обновленные параметры рецепта", required = true) @RequestBody RecipeDto recipeDto) {
        recipeDto = recipeService.saveRecipe(recipeDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удаляет рецепт", description = "Удаляет рецепт с заданным идентификатором")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно"),
            @ApiResponse(responseCode = "400", description = "Указан некорректный идентификатор"),
            @ApiResponse(responseCode = "404", description = "Рецепт не найден")
    })
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{recipe_id}")
    public ResponseEntity<?> deleteRecipeByID(@Parameter(description = "Идентификатор удаляемого рецепта", required = true) @PathVariable(value="recipe_id") Long recipeID){
        recipeService.removeRecipe(recipeID);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Возвращает список изображений для рецепта", description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedFiles.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Некорректный идентификатор рецепта")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{recipe_id}/image")
    public ResponseEntity<?> getAllImages(@Parameter(description = "Идентификатор рецепта", required = true) @PathVariable(value="recipe_id") Long recipeId) {
        LinkedFiles linkedFiles = uploadFileService.getUploadedFileListByResource(recipeId, Recipe.class);
        linkedFiles = FileController.transformUriInLinkedFiles(linkedFiles);
        return ResponseEntity.ok().body(linkedFiles);
    }

    @Operation(summary = "Добавляет новое изображение к рецепту", description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно", headers = @Header(name = "Location", description = "URI нового изображения рецепта")),
            @ApiResponse(responseCode = "405", description = "Некорректные входные данные")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{recipe_id}/image")
    public ResponseEntity<?> addImage(@Parameter(description = "Идентификатор рецепта", required = true) @PathVariable(value = "recipe_id") Long recipeId,
                                      @Parameter(description = "Составляющая часть ресурса (шаг рецепта)", required = false) @RequestHeader(value = "Resource-Part", required = false, defaultValue = "") String resourcePart,
                                      @Parameter(description = "Уникальное имя файла и описание изображения", required = true) @RequestBody FileLinkDto fileLink){
        UploadedFileLinkDto uploadedFileLinkDto = UploadedFileLinkDto.builder()
                .filename(fileLink.getFileKey())
                .objectId(recipeId)
                .objectType(Recipe.class.getSimpleName())
                .objectPart(resourcePart)
                .description(fileLink.getDescription())
                .build();
        UploadedFileLink uploadedFileLink = uploadFileService.saveUploadFileLink(uploadedFileLinkDto);
        URI newImageLinkURI = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{filekey}")
                .buildAndExpand(uploadedFileLink.getUploadedFile().getFilename())
                .toUri();
        return ResponseEntity.created(newImageLinkURI).build();
    }

    @Operation(summary = "Удаляет связь изображения с рецептом", description = "Удаляет связь рецепта с указанным идентификатором с заданным файлом")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно"),
            @ApiResponse(responseCode = "400", description = "Указан некорректный идентификатор рецепта"),
            @ApiResponse(responseCode = "404", description = "Связь рецепта с изображением не найдена")
    })
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{recipe_id}/image/{filekey:.+}")
    public ResponseEntity<?> removeImage(@Parameter(description = "Идентификатор рецепта", required = true) @PathVariable(value = "recipe_id") Long recipeId,
                                         @Parameter(description = "Уникальное имя файла", required = true) @PathVariable(value = "filekey") String fileKey,
                                         @Parameter(description = "Составляющая часть ресурса (шаг рецепта)", required = false) @RequestHeader(value = "Resource-Part", required = false, defaultValue = "") String resourcePart){
        UploadedFileLinkDto uploadedFileLinkDto = UploadedFileLinkDto.builder()
                .filename(fileKey)
                .objectId(recipeId)
                .objectType(Recipe.class.getSimpleName())
                .objectPart(resourcePart)
                .build();
        uploadFileService.removeUploadedFileLink(uploadedFileLinkDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Возвращает список тэгов для рецепта", description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно",
                    content = {
                            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = HashTag.class)))
                    }),
            @ApiResponse(responseCode = "400", description = "Некорректный идентификатор рецепта")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{recipe_id}/tag")
    public ResponseEntity<?> getAllTags(@Parameter(description = "Идентификатор рецепта", required = true) @PathVariable(value="recipe_id") Long recipeId) {
        Set<HashTag> tags = recipeService.getTagsById(recipeId);
        return ResponseEntity.ok().body(tags);
    }

    @Operation(summary = "Добавляет тэг к рецепту", description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно", headers = @Header(name = "Location", description = "URI нового тэга рецепта")),
            @ApiResponse(responseCode = "404", description = "Рецепт или тэг не найден"),
            @ApiResponse(responseCode = "405", description = "Некорректные входные данные")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{recipe_id}/tag/{tag_id}")
    public ResponseEntity<?> addTag(@Parameter(description = "Идентификатор рецепта", required = true) @PathVariable(value = "recipe_id") Long recipeId,
                                    @Parameter(description = "Идентификатор тэга", required = true) @PathVariable(value = "tag_id") Long tagId){
        recipeService.addTagToRecipe(recipeId, tagId);
        URI newTagURI = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        return ResponseEntity.created(newTagURI).build();
    }

    @Operation(summary = "Удаляет тэг с рецепта", description = "Удаляет связь рецепта с указанным идентификатором с заданным тэгом")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно"),
            @ApiResponse(responseCode = "400", description = "Указан некорректный идентификатор рецепта или еэга"),
            @ApiResponse(responseCode = "404", description = "Рецепт или тэг не найден")
    })
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{recipe_id}/tag/{tag_id}")
    public ResponseEntity<?> removeTag(@Parameter(description = "Идентификатор рецепта", required = true) @PathVariable(value = "recipe_id") Long recipeId,
                                       @Parameter(description = "Идентификатор тэга", required = true) @PathVariable(value = "tag_id") Long tagId){
        recipeService.removeTagFromRecipe(recipeId, tagId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Возвращает список оценок для рецепта", description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно",
                    content = {
                            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserRating.class)))
                    }),
            @ApiResponse(responseCode = "400", description = "Некорректный идентификатор рецепта")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{recipe_id}/rating")
    public ResponseEntity<?> getAllRatings(@Parameter(description = "Идентификатор рецепта", required = true) @PathVariable(value="recipe_id") Long recipeId) {
        List<UserRating> ratings = ratingService.getAllRecipeRatings(recipeId);
        return ResponseEntity.ok().body(ratings);
    }

    @Operation(summary = "Сохраняет оценку пользователя для рецепта", description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно"),
            @ApiResponse(responseCode = "404", description = "Рецепт или пользователь не найден"),
            @ApiResponse(responseCode = "405", description = "Некорректные входные данные")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/{recipe_id}/rating/{user_id}", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<?> saveRating(@Parameter(description = "Идентификатор рецепта", required = true) @PathVariable(value = "recipe_id") Long recipeId,
                                        @Parameter(description = "Идентификатор пользователя", required = true) @PathVariable(value = "user_id") Long userId,
                                        @Parameter(description = "Оценка рецепта", required = true) @RequestBody @Valid UserRatingDto userRatingDto){
        User user = userService.getUser(userId);
        UserRating rating = ratingService.saveRating(recipeId, user.getId(), userRatingDto.getRate());
        RatingChangedEvent event = new RatingChangedEvent(rating.getRecipe(), user, rating);
        eventPublisher.publishEvent(event);
        URI newRatingURI = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        return ResponseEntity.created(newRatingURI).build();
    }

    @Operation(summary = "Возвращает оценку рецепта пользователем", description = "Возвращает оценку данного рецепта заданным пользователем")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = UserRating.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Указан некорректный идентификатор"),
            @ApiResponse(responseCode = "404", description = "Оценка не найдена")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{recipe_id}/rating/{user_id}")
    public ResponseEntity<?> getRecipeUserRating(@Parameter(description = "Идентификатор рецепта", required = true) @PathVariable(value="recipe_id") Long recipeId,
                                                 @Parameter(description = "Идентификатор пользователя", required = true) @PathVariable(value = "user_id") Long userId){
        UserRating rating = ratingService.getRecipeRatingByUserId(recipeId, userId);
        return ResponseEntity.ok(rating);
    }

    @Operation(summary = "Удаляет оценку пользователя для рецепта", description = "Удаляет оценку заданного пользователя для рецепта с указанным идентификатором")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно"),
            @ApiResponse(responseCode = "400", description = "Некорректный идентификатор рецепта или пользователя"),
            @ApiResponse(responseCode = "404", description = "Оценка не найдена")
    })
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{recipe_id}/rating/{user_id}")
    public ResponseEntity<?> removeRating(@Parameter(description = "Идентификатор рецепта", required = true) @PathVariable(value = "recipe_id") Long recipeId,
                                          @Parameter(description = "Идентификатор пользователя", required = true) @PathVariable(value = "user_id") Long userId){
        ratingService.removeRating(recipeId, userId);
        return ResponseEntity.ok().build();
    }

}


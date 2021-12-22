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
import ru.geekbrains.cookbook.domain.HashTag;
import ru.geekbrains.cookbook.service.TagService;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/tag")
@Tag(name = "Теги для рецептов", description = "API для тегов рецептов")
public class TagController {
    private TagService tagService;

    @Operation(summary = "Возвращает список тегов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = HashTag.class)))}
            )}
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<HashTag> getAllTags(){
        return tagService.findAll();
    }

    @Operation(summary = "Возвращает тег по идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = HashTag.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Указан некорректный идентификатор"),
            @ApiResponse(responseCode = "404", description = "Тег не найден")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{tag_id}")
    public HashTag getTagByID(@Parameter(description = "Идентификатор тега", required = true) @PathVariable(value="tag_id") Long tagID){
        return tagService.getTagById(tagID);
    }

    @Operation(summary = "Создает новый тег", description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = HashTag.class))
                    }),
            @ApiResponse(responseCode = "405", description = "Некорректные входные данные")
    })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    public ResponseEntity<?> addTag(@Parameter(description = "Новый тег", required = true) @RequestBody HashTag tag) {
        tag.setId(null);
        tag = tagService.saveTag(tag);
        return ResponseEntity.ok(tag);
    }

    @Operation(summary = "Удаляет тег", description = "Удаляет тег с заданным идентификатором")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно"),
            @ApiResponse(responseCode = "400", description = "Указан некорректный идентификатор"),
            @ApiResponse(responseCode = "404", description = "Тег не найден")
    })
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{tag_id}")
    public ResponseEntity<?> deleteIngredientByID(@Parameter(description = "Идентификатор удаляемого тега", required = true) @PathVariable(value="tag_id") Long tagID){
        tagService.removeTag(tagID);
        return ResponseEntity.ok().build();
    }

}

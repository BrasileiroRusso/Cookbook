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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.geekbrains.cookbook.domain.Ingredient;
import ru.geekbrains.cookbook.domain.file.LinkedFiles;
import ru.geekbrains.cookbook.domain.file.UploadedFileLink;
import ru.geekbrains.cookbook.dto.FileLinkDto;
import ru.geekbrains.cookbook.dto.IngredientDto;
import ru.geekbrains.cookbook.dto.UploadedFileLinkDto;
import ru.geekbrains.cookbook.service.IngredientService;
import ru.geekbrains.cookbook.service.UploadFileService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/ingredient")
@Tag(name = "Ингредиенты", description = "API для ингредиентов")
public class IngredientController {
    private IngredientService ingredientService;
    private UploadFileService uploadFileService;

    @Operation(summary = "Возвращает список ингредиентов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = IngredientDto.class)))}
            )}
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<?> getAllIngredients(){
        List<IngredientDto> ingredientList = ingredientService.findAll();
        return ResponseEntity.ok(ingredientList);
    }

    @Operation(summary = "Возвращает категорию по идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = IngredientDto.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Указан некорректный идентификатор"),
            @ApiResponse(responseCode = "404", description = "Ингредиент не найден")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{ingredient_id}")
    public ResponseEntity<?> getIngredientByID(@Parameter(description = "Идентификатор ингредиента", required = true) @PathVariable(value="ingredient_id") Long ingredientID){
        IngredientDto ingredient = ingredientService.getIngredientById(ingredientID);
        return ResponseEntity.ok(ingredient);
    }

    @Operation(summary = "Создает новый ингредиент", description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно", headers = @Header(name = "Location", description = "URI нового ингредиента")),
            @ApiResponse(responseCode = "405", description = "Некорректные входные данные")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<?> addIngredient(@Parameter(description = "Новый ингредиент", required = true) @RequestBody @Valid IngredientDto ingredientDto) {
        ingredientDto.setId(null);
        ingredientDto = ingredientService.saveIngredient(ingredientDto);
        URI newIngredientURI = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{ingredient_id}")
                .buildAndExpand(ingredientDto.getId())
                .toUri();
        return ResponseEntity.created(newIngredientURI).build();
    }

    @Operation(summary = "Обновляет существующий ингредиент", description = "Обновляет существующий ингредиент с заданным идентификатором")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно"),
            @ApiResponse(responseCode = "400", description = "Указан некорректный идентификатор"),
            @ApiResponse(responseCode = "404", description = "Ингредиент не найден"),
            @ApiResponse(responseCode = "405", description = "Некорректные входные данные")
    })
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(path = "/{ingredient_id}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> updateIngredient(@Parameter(description = "Идентификатор ингредиента", required = true) @PathVariable(value="ingredient_id") Long ingredientId,
                                              @Parameter(description = "Обновленные параметры ингредиента", required = true) @RequestBody @Valid IngredientDto ingredientDto) {
        ingredientDto.setId(ingredientId);
        ingredientDto = ingredientService.saveIngredient(ingredientDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удаляет ингредиент", description = "Удаляет ингредиент с заданным идентификатором")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно"),
            @ApiResponse(responseCode = "400", description = "Указан некорректный идентификатор"),
            @ApiResponse(responseCode = "404", description = "Ингредиент не найден")
    })
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{ingredient_id}")
    public ResponseEntity<?> deleteIngredientByID(@Parameter(description = "Идентификатор удаляемого ингредиента", required = true) @PathVariable(value="ingredient_id") Long ingredientID){
        ingredientService.removeIngredient(ingredientID);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Возвращает список изображений для ингредиента", description = "Возвращает список изображений для ингредиента")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedFiles.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Некорректный идентификатор ингредиента")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{ingredient_id}/image")
    public ResponseEntity<?> getAllImages(@Parameter(description = "Идентификатор ингредиента", required = true) @PathVariable(value="ingredient_id") Long ingredientId) {
        LinkedFiles linkedFiles = uploadFileService.getUploadedFileListByResource(ingredientId, Ingredient.class);
        linkedFiles = FileController.transformUriInLinkedFiles(linkedFiles);
        return ResponseEntity.ok().body(linkedFiles);
    }

    @Operation(summary = "Добавляет новое изображение к ингредиенту", description = "Добавляет новое изображение к ингредиенту")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно", headers = @Header(name = "Location", description = "URI нового изображения ингредиента")),
            @ApiResponse(responseCode = "405", description = "Некорректные входные данные")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{ingredient_id}/image")
    public ResponseEntity<?> addImage(@Parameter(description = "Идентификатор ингредиента", required = true) @PathVariable(value = "ingredient_id") Long ingredientId,
                                      @Parameter(description = "Уникальное имя файла и описание изображения", required = true) @RequestBody FileLinkDto fileLink){
        UploadedFileLinkDto uploadedFileLinkDto = UploadedFileLinkDto.builder()
                .filename(fileLink.getFileKey())
                .objectId(ingredientId)
                .objectType(Ingredient.class.getSimpleName())
                .objectPart("")
                .description(fileLink.getDescription())
                .build();
        UploadedFileLink uploadedFileLink = uploadFileService.saveUploadFileLink(uploadedFileLinkDto);
        URI newImageLinkURI = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{filekey}")
                .buildAndExpand(uploadedFileLink.getUploadedFile().getFilename())
                .toUri();
        return ResponseEntity.created(newImageLinkURI).build();
    }

    @Operation(summary = "Удаляет связь изображения с ингредиентом", description = "Удаляет связь ингредиента с указанным идентификатором с заданным файлом")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно"),
            @ApiResponse(responseCode = "400", description = "Указан некорректный идентификатор ингредиента"),
            @ApiResponse(responseCode = "404", description = "Связь ингредиента с изображением не найдена")
    })
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{ingredient_id}/image/{filekey:.+}")
    public ResponseEntity<?> removeImage(@Parameter(description = "Идентификатор ингредиента", required = true) @PathVariable(value = "ingredient_id") Long ingredientId,
                                         @Parameter(description = "Уникальное имя файла", required = true) @PathVariable(value = "filekey") String fileKey){
        UploadedFileLinkDto uploadedFileLinkDto = UploadedFileLinkDto.builder()
                .filename(fileKey)
                .objectId(ingredientId)
                .objectType(Ingredient.class.getSimpleName())
                .objectPart("")
                .build();
        uploadFileService.removeUploadedFileLink(uploadedFileLinkDto);
        return ResponseEntity.ok().build();
    }

}

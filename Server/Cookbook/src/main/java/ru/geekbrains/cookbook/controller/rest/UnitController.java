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
import ru.geekbrains.cookbook.dto.UnitDto;
import ru.geekbrains.cookbook.service.UnitService;

import javax.validation.Valid;
import java.net.URI;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/unit")
@Tag(name = "Единицы измерения ингредиентов", description = "API для единиц измерения")
public class UnitController {
    private UnitService unitService;

    @Operation(summary = "Возвращает список единиц измерения")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UnitDto.class)))}
            )}
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<?> getAllUnits(){
        return ResponseEntity.ok(unitService.findAll());
    }

    @Operation(summary = "Возвращает единицу измерения по идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = UnitDto.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Указан некорректный идентификатор единицы измерения"),
            @ApiResponse(responseCode = "404", description = "Единица измерения не найдена")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{unit_id}")
    public ResponseEntity<?> getUnitByID(@Parameter(description = "Идентификатор единицы измерения", required = true) @PathVariable(value="unit_id") Long unitID){
        return ResponseEntity.ok(unitService.getUnitById(unitID));
    }

    @Operation(summary = "Создает новую единицу измерения", description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно", headers = @Header(name = "Location", description = "URI новой единицы измерения")),
            @ApiResponse(responseCode = "405", description = "Некорректные входные данные")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<?> addUnit(@Parameter(description = "Новая единица измерения", required = true) @RequestBody @Valid UnitDto unitDto) {
        unitDto.setId(null);
        unitDto = unitService.saveUnit(unitDto);
        URI newUnitURI = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{unit_id}")
                .buildAndExpand(unitDto.getId())
                .toUri();
        return ResponseEntity.created(newUnitURI).build();
    }

    @Operation(summary = "Обновляет существующую единицу измерения", description = "Обновляет существующую единицу измерения с заданным идентификатором")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно"),
            @ApiResponse(responseCode = "400", description = "Указан некорректный идентификатор"),
            @ApiResponse(responseCode = "404", description = "Единица измерения не найдена"),
            @ApiResponse(responseCode = "405", description = "Некорректные входные данные")
    })
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(path = "/{unit_id}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> updateUnit(@Parameter(description = "Идентификатор единицы измерения", required = true) @PathVariable(value="unit_id") Long unitId,
                                        @Parameter(description = "Обновленные параметры единицы измерения", required = true) @RequestBody @Valid UnitDto unitDto) {
        unitDto.setId(unitId);
        unitDto = unitService.saveUnit(unitDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Обновляет меру данной единицы измерения относительно основной единицы", description = "Обновляет меру данной единицы измерения относительно основной единицы")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно"),
            @ApiResponse(responseCode = "400", description = "Указан некорректный идентификатор"),
            @ApiResponse(responseCode = "404", description = "Единица измерения не найдена"),
            @ApiResponse(responseCode = "405", description = "Некорректные входные данные")
    })
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(path = "/{unit_id}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> updateMeasure(@Parameter(description = "Идентификатор единицы измерения", required = true) @PathVariable(value="unit_id") Long unitId,
                                           @Parameter(description = "Обновленная мера для единицы измерения", required = true) @RequestBody UnitDto unitDto) {
        unitDto.setId(unitId);
        unitDto = unitService.saveMeasure(unitDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удаляет единицу измерения", description = "Удаляет единицу измерения с заданным идентификатором")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно"),
            @ApiResponse(responseCode = "400", description = "Указан некорректный идентификатор"),
            @ApiResponse(responseCode = "404", description = "Единица измерения не найдена")
    })
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{unit_id}")
    public ResponseEntity<?> deleteUnitByID(@Parameter(description = "Идентификатор единицы измерения", required = true) @PathVariable(value="unit_id") Long unitID){
        unitService.removeUnit(unitID);
        return ResponseEntity.ok().build();
    }

}

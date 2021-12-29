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
import ru.geekbrains.cookbook.dto.UnitTypeDto;
import ru.geekbrains.cookbook.service.UnitService;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/unittype")
@AllArgsConstructor
@Tag(name = "Типы единиц измерения количества ингредиентов", description = "API для типов единиц измерения количества ингредиентов")
public class UnitTypeController {
    private UnitService unitService;

    @Operation(summary = "Возвращает список типов единиц измерения")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно",
                         content = { @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UnitTypeDto.class)))}
            )}
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<?> getAllUnitTypes(){
        return ResponseEntity.ok(unitService.findAllUnitTypes());
    }

    @Operation(summary = "Возвращает тип единицы измерения по идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно",
                         content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UnitTypeDto.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Указан некорректный идентификатор типа единицы измерения"),
            @ApiResponse(responseCode = "404", description = "Тип единицы измерения не найден")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{unit_type_id}")
    public ResponseEntity<?> getUnitTypeByID(@Parameter(description = "Идентификатор единицы измерения", required = true) @PathVariable(value="unit_type_id") Long unitTypeId){
        return ResponseEntity.ok(unitService.getUnitTypeById(unitTypeId));
    }

    @Operation(summary = "Создает новый тип единицы измерения", description = "Создает новый тип единицы измерения")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно", headers = @Header(name = "Location", description = "URI нового типа единицы измерения")),
            @ApiResponse(responseCode = "405", description = "Некорректные входные данные")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<?> addUnitType(@Parameter(description = "Новый тип единицы измерения", required = true) @RequestBody UnitTypeDto unitTypeDto) {
        unitTypeDto.setId(null);
        unitTypeDto = unitService.saveUnitType(unitTypeDto);
        URI newURI = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{unit_type_id}")
                .buildAndExpand(unitTypeDto.getId())
                .toUri();
        return ResponseEntity.created(newURI).build();
    }

    @Operation(summary = "Обновляет существующий тип единиц измерения", description = "Обновляет существующий тип единиц измерения с заданным идентификатором")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно"),
            @ApiResponse(responseCode = "400", description = "Указан некорректный идентификатор"),
            @ApiResponse(responseCode = "404", description = "Тип единиц измерения не найден"),
            @ApiResponse(responseCode = "405", description = "Некорректные входные данные")
    })
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(path = "/{unit_type_id}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> updateUnit(@Parameter(description = "Идентификатор единицы измерения", required = true) @PathVariable(value="unit_type_id") Long unitTypeId,
                                        @Parameter(description = "Обновленные параметры типа единиц измерения", required = true) @RequestBody UnitTypeDto unitTypeDto) {
        unitTypeDto.setId(unitTypeId);
        unitTypeDto = unitService.saveUnitType(unitTypeDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Устанавливает базовую единицу измерения для единиц заданного типа", description = "Устанавливает базовую единицу измерения для единиц заданного типа")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно"),
            @ApiResponse(responseCode = "400", description = "Указан некорректный идентификатор"),
            @ApiResponse(responseCode = "404", description = "Тип единиц измерения не найден"),
            @ApiResponse(responseCode = "405", description = "Некорректные входные данные")
    })
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(path = "/{unit_type_id}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> setBaseUnit(@Parameter(description = "Идентификатор типа единиц измерения", required = true) @PathVariable(value="unit_type_id") Long unitTypeId,
                                         @Parameter(description = "Базовая единица измерения", required = true) @RequestBody UnitTypeDto unitTypeDto) {
        unitService.setMainUnit(unitTypeDto);
        return ResponseEntity.ok().build();
    }
}

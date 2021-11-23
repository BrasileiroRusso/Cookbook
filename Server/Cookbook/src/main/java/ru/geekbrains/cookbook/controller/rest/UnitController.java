package ru.geekbrains.cookbook.controller.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.cookbook.controller.rest.response.ErrorResponse;
import ru.geekbrains.cookbook.controller.rest.response.OKResponse;
import ru.geekbrains.cookbook.domain.Unit;
import ru.geekbrains.cookbook.service.UnitService;
import ru.geekbrains.cookbook.service.exception.UnitCannotDeleteException;
import ru.geekbrains.cookbook.service.exception.UnitNotFoundException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/rest/unit")
public class UnitController {
    private UnitService unitService;

    @GetMapping
    public List<Unit> getAllUnits(){
        return unitService.findAll();
    }

    @GetMapping("/{unit_id}")
    public Unit getUnitByID(@PathVariable(value="unit_id") Long unitID){
        return unitService.getUnitById(unitID);
    }

    @PostMapping
    public ResponseEntity<OKResponse> addUnit(@RequestBody Unit unit) {
        System.out.println("Rest Unit POST: " + unit);
        unit.setId(null);
        unit = unitService.saveUnit(unit);
        return new ResponseEntity<>(new OKResponse(unit.getId(), System.currentTimeMillis()), HttpStatus.CREATED);
    }

    @PutMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<OKResponse> updateUnit(@RequestBody Unit unit) {
        System.out.println("Rest Unit PUT: " + unit);
        unit = unitService.saveUnit(unit);
        return new ResponseEntity<>(new OKResponse(unit.getId(), System.currentTimeMillis()), HttpStatus.OK);
    }

    @DeleteMapping("/{unit_id}")
    public ResponseEntity<OKResponse> deleteUnitByID(@PathVariable(value="unit_id") Long unitID){
        System.out.println("Rest Unit DELETE: " + unitID);
        unitService.removeUnit(unitID);
        return new ResponseEntity<>(new OKResponse(unitID, System.currentTimeMillis()), HttpStatus.OK);
    }

    @ExceptionHandler({UnitNotFoundException.class, UnitCannotDeleteException.class})
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        System.out.println("Rest Unit ERROR");
        ErrorResponse ErrorResponse = new ErrorResponse();
        ErrorResponse.setMessage(e.getMessage());
        ErrorResponse.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(ErrorResponse, HttpStatus.NOT_FOUND);
    }
}

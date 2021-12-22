package ru.geekbrains.cookbook.service;

import ru.geekbrains.cookbook.dto.UnitDto;
import ru.geekbrains.cookbook.dto.UnitTypeDto;
import java.util.List;

public interface UnitService {
    List<UnitDto> findAll();
    List<UnitDto> findAll(Long unitTypeId);
    UnitDto getUnitById(Long unitId);
    UnitDto saveUnit(UnitDto unitDto);
    boolean removeUnit(Long unitId);
    List<UnitTypeDto> findAllUnitTypes();
    UnitTypeDto getUnitTypeById(Long unitTypeId);
    UnitTypeDto saveUnitType(UnitTypeDto unitTypeDto);
    boolean removeUnitType(Long unitTypeId);
    void setMainUnit(UnitTypeDto unitTypeDto);
    UnitDto saveMeasure(UnitDto unitDto);
}


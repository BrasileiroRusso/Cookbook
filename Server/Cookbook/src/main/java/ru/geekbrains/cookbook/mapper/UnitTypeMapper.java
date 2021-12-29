package ru.geekbrains.cookbook.mapper;

import ru.geekbrains.cookbook.domain.Unit;
import ru.geekbrains.cookbook.domain.UnitType;
import ru.geekbrains.cookbook.dto.UnitDto;
import ru.geekbrains.cookbook.dto.UnitTypeDto;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UnitTypeMapper {
    public static UnitTypeDto unitTypeToDto(UnitType unitType){
        Long baseUnitId = null;
        List<UnitDto> unitDtoList = null;
        if(unitType.getUnits() != null){
            baseUnitId = unitType.getUnits().stream()
                    .filter(Unit::isMain)
                    .map(Unit::getId)
                    .findFirst()
                    .orElse(null);
            unitDtoList = UnitMapper.unitListToDtoList(new ArrayList<>(unitType.getUnits()));
        }

        UnitTypeDto unitTypeDto = UnitTypeDto.builder()
                .id(unitType.getId())
                .briefName(unitType.getBriefName())
                .name(unitType.getName())
                .description(unitType.getDescription())
                .units(unitDtoList)
                .baseUnitId(baseUnitId)
                .build();
        return unitTypeDto;
    }

    public static List<UnitTypeDto> unitTypeListToDtoList(List<UnitType> unitTypeList){
        if(unitTypeList == null)
            return null;
        return unitTypeList.stream().map(UnitTypeMapper::unitTypeToDto).collect(Collectors.toList());
    }
}

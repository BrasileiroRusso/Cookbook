package ru.geekbrains.cookbook.mapper;

import ru.geekbrains.cookbook.domain.Unit;
import ru.geekbrains.cookbook.dto.UnitDto;
import java.util.List;
import java.util.stream.Collectors;

public class UnitMapper {
    public static UnitDto unitToDto(Unit unit){
        UnitDto unitDto = UnitDto.builder()
                .id(unit.getId())
                .briefName(unit.getBriefName())
                .name(unit.getName())
                .measure(unit.getMeasure())
                .build();
        if(unit.getUnitType() != null){
            unitDto.setUnitTypeId(unit.getUnitType().getId());
            unitDto.setUnitType(unit.getUnitType().getName());
        }
        return unitDto;
    }

    public static List<UnitDto> unitListToDtoList(List<Unit> unitList){
        if(unitList == null)
            return null;
        return unitList.stream().map(UnitMapper::unitToDto).collect(Collectors.toList());
    }
}

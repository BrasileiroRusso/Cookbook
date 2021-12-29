package ru.geekbrains.cookbook.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.geekbrains.cookbook.domain.Unit;
import ru.geekbrains.cookbook.domain.UnitType;
import ru.geekbrains.cookbook.dto.UnitDto;
import ru.geekbrains.cookbook.dto.UnitTypeDto;
import ru.geekbrains.cookbook.mapper.UnitMapper;
import ru.geekbrains.cookbook.mapper.UnitTypeMapper;
import ru.geekbrains.cookbook.repository.UnitRepository;
import ru.geekbrains.cookbook.repository.UnitTypeRepository;
import ru.geekbrains.cookbook.service.UnitService;
import ru.geekbrains.cookbook.service.exception.MainUnitNotDefined;
import ru.geekbrains.cookbook.service.exception.ResourceCannotDeleteException;
import ru.geekbrains.cookbook.service.exception.ResourceNotFoundException;
import ru.geekbrains.cookbook.service.exception.UnitIncorrectTypeException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UnitServiceImpl implements UnitService {
    private static final int MEASURE_SCALE = 10;

    private final UnitRepository unitRepository;
    private final UnitTypeRepository unitTypeRepository;

    @Override
    @Transactional
    public List<UnitDto> findAll() {
        return UnitMapper.unitListToDtoList(unitRepository.findAll());
    }

    @Override
    @Transactional
    public List<UnitDto> findAll(Long unitTypeId) {
        return UnitMapper.unitListToDtoList(unitRepository.findAllByUnitType_Id(unitTypeId));
    }

    @Override
    @Transactional
    public UnitDto getUnitById(Long id)  {
        Unit unit = findUnitById(id);
        return UnitMapper.unitToDto(unit);
    }

    @Override
    @Transactional
    public UnitDto saveUnit(UnitDto unitDto) {
        Unit unit;
        if(unitDto.getId() != null){
            unit = findUnitById(unitDto.getId());
        }
        else{
            unit = new Unit();
        }
        unit.setBriefName(unitDto.getBriefName());
        unit.setName(unitDto.getName());
        if(unit.getUnitType() == null || !unitDto.getUnitTypeId().equals(unit.getUnitType().getId())){
            unit.setUnitType(findUnitTypeById(unitDto.getUnitTypeId()));
            unit.setMain(false);
        }
        updateUnitMeasure(unit, unitDto.getMeasure());
        unit = unitRepository.save(unit);
        return UnitMapper.unitToDto(unit);
    }

    @Override
    @Transactional
    public boolean removeUnit(Long unitId) {
        try{
            Unit unit = findUnitById(unitId);
            unitRepository.delete(unit);
            return true;
        }
        catch(DataIntegrityViolationException e){
            ResourceCannotDeleteException exc = new ResourceCannotDeleteException();
            exc.initCause(e);
            throw exc;
        }
    }

    @Override
    @Transactional
    public List<UnitTypeDto> findAllUnitTypes() {
        List<UnitType> unitTypes = unitTypeRepository.findAll();
        return UnitTypeMapper.unitTypeListToDtoList(unitTypes);
    }

    @Override
    @Transactional
    public UnitTypeDto getUnitTypeById(Long unitTypeId) {
        UnitType unitType = findUnitTypeById(unitTypeId);
        return UnitTypeMapper.unitTypeToDto(unitType);
    }

    @Override
    @Transactional
    public UnitTypeDto saveUnitType(UnitTypeDto unitTypeDto) {
        UnitType unitType;
        if(unitTypeDto.getId() != null){
            unitType = findUnitTypeById(unitTypeDto.getId());
            if(unitTypeDto.getBaseUnitId() != null){
                setMainUnit(unitTypeDto);
            }
        }
        else{
            unitType = new UnitType();
        }
        unitType.setBriefName(unitTypeDto.getBriefName());
        unitType.setName(unitTypeDto.getName());
        unitType.setDescription(unitTypeDto.getDescription());
        unitType = unitTypeRepository.save(unitType);
        return UnitTypeMapper.unitTypeToDto(unitType);
    }

    @Override
    @Transactional
    public boolean removeUnitType(Long unitTypeId) {
        try{
            UnitType unitType = findUnitTypeById(unitTypeId);
            unitTypeRepository.delete(unitType);
            return true;
        }
        catch(DataIntegrityViolationException e){
            ResourceCannotDeleteException exc = new ResourceCannotDeleteException();
            exc.initCause(e);
            throw exc;
        }
    }

    @Override
    @Transactional
    public void setMainUnit(UnitTypeDto unitTypeDto){
        Long unitId = unitTypeDto.getBaseUnitId();
        if(unitId != null){
            Unit baseUnit = findUnitById(unitId);
            if(!baseUnit.getUnitType().getId().equals(unitTypeDto.getId()))
                throw new UnitIncorrectTypeException();
            else
                setMainUnit(unitId);
        }
    }

    @Override
    @Transactional
    public UnitDto saveMeasure(UnitDto unitDto) {
        Unit unit = findUnitById(unitDto.getId());
        updateUnitMeasure(unit, unitDto.getMeasure());
        unit = unitRepository.save(unit);
        return UnitMapper.unitToDto(unit);
    }

    private void updateUnitMeasure(Unit unit, BigDecimal measure){
        if(measure == null || measure.equals(BigDecimal.ZERO)){
            unit.setMeasure(BigDecimal.ZERO);
            return;
        }
        Unit mainUnit = unitRepository.findFirstByUnitTypeAndMainIsTrue(unit.getUnitType()).orElseThrow(MainUnitNotDefined::new);
        if(!mainUnit.getId().equals(unit.getId()))
            unit.setMeasure(measure);
    }

    private void setMainUnit(Long unitId) {
        Unit unit = findUnitById(unitId);
        BigDecimal measure = unit.getMeasure();
        UnitType unitType = unit.getUnitType();
        Set<Unit> units = unitType.getUnits();
        for(Unit curUnit: units){
            curUnit.setMain(false);
            if(measure.compareTo(BigDecimal.ZERO) > 0)
                curUnit.setMeasure(curUnit.getMeasure().divide(measure, MEASURE_SCALE, RoundingMode.HALF_UP));
            else
                curUnit.setMeasure(BigDecimal.ZERO);
        }
        unit.setMain(true);
        unit.setMeasure(BigDecimal.ONE);
        unitRepository.saveAll(units);
    }

    private Unit findUnitById(Long unitId){
        return unitRepository.findById(unitId).orElseThrow(ResourceNotFoundException::new);
    }

    private UnitType findUnitTypeById(Long unitTypeId){
        return unitTypeRepository.findById(unitTypeId).orElseThrow(ResourceNotFoundException::new);
    }

}




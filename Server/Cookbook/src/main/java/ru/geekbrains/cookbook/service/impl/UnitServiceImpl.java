package ru.geekbrains.cookbook.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.geekbrains.cookbook.domain.Unit;
import ru.geekbrains.cookbook.repository.UnitRepository;
import ru.geekbrains.cookbook.service.UnitService;
import ru.geekbrains.cookbook.service.exception.UnitCannotDeleteException;
import ru.geekbrains.cookbook.service.exception.UnitNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UnitServiceImpl implements UnitService {
    private final UnitRepository unitRepository;

    @Override
    public List<Unit> findAll() {
        return unitRepository.findAll();
    }

    @Override
    @Transactional
    public Unit getUnitById(Long id)  {
        return unitRepository.findById(id).orElseThrow(UnitNotFoundException::new);
    }

    @Override
    @Transactional
    public Unit saveUnit(Unit unit) {
        if(unit.getId() != null){
            Optional<Unit> optionalUnit = unitRepository.findById(unit.getId());
            if(!optionalUnit.isPresent())
                throw new UnitNotFoundException(String.format("Unit with ID=%d doesn't exist", unit.getId()));
        }

        unit = unitRepository.save(unit);
        return unit;
    }

    @Override
    @Transactional
    public boolean removeUnit(Long id) {
        try{
            unitRepository.deleteById(id);
            return true;
        }
        catch(EmptyResultDataAccessException e){
            UnitNotFoundException exc = new UnitNotFoundException(String.format("Unit with ID=%d doesn't exist", id));
            exc.initCause(e);
            throw exc;
        }
        catch(DataIntegrityViolationException e){
            UnitCannotDeleteException exc = new UnitCannotDeleteException(String.format("Cannot remove the unit with ID=%d cause it has linked recipes", id));
            exc.initCause(e);
            throw exc;
        }
    }

}




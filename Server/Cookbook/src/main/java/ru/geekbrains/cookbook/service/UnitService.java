package ru.geekbrains.cookbook.service;

import ru.geekbrains.cookbook.domain.Unit;
import java.util.List;

public interface UnitService {
    List<Unit> findAll();
    Unit getUnitById(Long id);
    Unit saveUnit(Unit recipe);
    boolean removeUnit(Long id);
}


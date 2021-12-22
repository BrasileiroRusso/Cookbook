package ru.geekbrains.cookbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.geekbrains.cookbook.domain.Unit;
import ru.geekbrains.cookbook.domain.UnitType;

import java.util.List;
import java.util.Optional;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {
    List<Unit> findAllByUnitType_Id(Long unitTypeId);
    Optional<Unit> findFirstByUnitTypeAndMainIsTrue(UnitType unitType);
}

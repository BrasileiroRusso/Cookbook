package ru.geekbrains.cookbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.geekbrains.cookbook.domain.UnitType;

@Repository
public interface UnitTypeRepository extends JpaRepository<UnitType, Long> {
}

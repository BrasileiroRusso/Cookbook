package ru.geekbrains.cookbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.geekbrains.cookbook.domain.Unit;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {
}

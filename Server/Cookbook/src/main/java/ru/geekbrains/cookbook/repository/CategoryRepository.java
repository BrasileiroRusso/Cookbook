package ru.geekbrains.cookbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.geekbrains.cookbook.domain.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}

package ru.geekbrains.cookbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.geekbrains.cookbook.domain.HashTag;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<HashTag, Long> {
}

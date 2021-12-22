package ru.geekbrains.cookbook.service;

import ru.geekbrains.cookbook.domain.HashTag;
import java.util.List;

public interface TagService {
    List<HashTag> findAll();
    HashTag getTagById(Long id);
    HashTag saveTag(HashTag tag);
    boolean removeTag(Long id);
}


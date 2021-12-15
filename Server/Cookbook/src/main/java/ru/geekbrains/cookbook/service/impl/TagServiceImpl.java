package ru.geekbrains.cookbook.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.geekbrains.cookbook.domain.HashTag;
import ru.geekbrains.cookbook.repository.TagRepository;
import ru.geekbrains.cookbook.service.TagService;
import ru.geekbrains.cookbook.service.exception.ResourceNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    @Override
    public List<HashTag> findAll() {
        return tagRepository.findAll();
    }

    @Override
    @Transactional
    public HashTag getTagById(Long id) {
        return tagRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    @Transactional
    public HashTag saveTag(HashTag tag) {
        HashTag newTag;
        if(tag.getId() != null){
            newTag = tagRepository.findById(tag.getId()).orElseThrow(ResourceNotFoundException::new);
            newTag.setName(tag.getName());
            newTag.setDescription(tag.getDescription());
        }
        else{
            newTag = tag;
        }
        newTag = tagRepository.save(newTag);
        return newTag;
    }

    @Override
    public boolean removeTag(Long id) {
        tagRepository.deleteById(id);
        return true;
    }
}

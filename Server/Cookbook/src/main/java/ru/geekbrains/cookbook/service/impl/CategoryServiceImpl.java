package ru.geekbrains.cookbook.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.geekbrains.cookbook.domain.Category;
import ru.geekbrains.cookbook.dto.CategoryDto;
import ru.geekbrains.cookbook.mapper.CategoryMapper;
import ru.geekbrains.cookbook.repository.CategoryRepository;
import ru.geekbrains.cookbook.repository.specification.CategorySpecification;
import ru.geekbrains.cookbook.service.CategoryService;
import ru.geekbrains.cookbook.service.exception.ResourceCannotDeleteException;
import ru.geekbrains.cookbook.service.exception.ResourceNotFoundException;
import java.util.List;

@Service("categoryService")
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public List<CategoryDto> findAll() {
        return CategoryMapper.categoryListToDtoList(categoryRepository.findAll());
    }

    @Override
    @Transactional
    public List<CategoryDto> findAll(Long parentId, Boolean childs) {
        List<Category> categoryList = categoryRepository.findAll(CategorySpecification.categoryFilter(parentId, childs));
        return CategoryMapper.categoryListToDtoList(categoryList);
    }

    @Override
    @Transactional
    public CategoryDto getCategoryById(Long id)  {
        Category category = findById(id);
        return CategoryMapper.categoryToDto(category);
    }

    @Override
    @Transactional
    public CategoryDto saveCategory(CategoryDto categoryDto) {
        Category category;
        if(categoryDto.getId() != null)
            category = findById(categoryDto.getId());
        else
            category = new Category();
        category.setTitle(categoryDto.getTitle());
        if(categoryDto.getParentId() != null){
            Category parentCategory = findById(categoryDto.getParentId());
            category.setParentCategory(parentCategory);
        }
        if(categoryDto.getParentId() == null)
            category.setParentCategory(null);
        category = categoryRepository.save(category);
        return CategoryMapper.categoryToDto(category);
    }

    @Override
    @Transactional
    public boolean removeCategory(Long id) {
        try{
            Category category = findById(id);
            categoryRepository.delete(category);
            return true;
        }
        catch(DataIntegrityViolationException e){
            ResourceCannotDeleteException exc = new ResourceCannotDeleteException();
            exc.initCause(e);
            throw exc;
        }
    }

    private Category findById(Long id){
        return categoryRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

}

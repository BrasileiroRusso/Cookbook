package ru.geekbrains.cookbook.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.geekbrains.cookbook.domain.Category;
import ru.geekbrains.cookbook.dto.CategoryDto;
import ru.geekbrains.cookbook.dto.CategoryDtoIn;
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
    public CategoryDto saveCategory(CategoryDtoIn categoryDto) {
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
            categoryRepository.deleteById(id);
            return true;
        }
        catch(EmptyResultDataAccessException e){
            ResourceNotFoundException exc = new ResourceNotFoundException(String.format("Category with ID=%d doesn't exist", id));
            exc.initCause(e);
            throw exc;
        }
        catch(DataIntegrityViolationException e){
            ResourceCannotDeleteException exc = new ResourceCannotDeleteException(String.format("Cannot remove the category with ID=%d cause it has linked recipes", id));
            exc.initCause(e);
            throw exc;
        }
    }

    private Category findById(Long id){
        return categoryRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

}

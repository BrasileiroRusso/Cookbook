package ru.geekbrains.cookbook.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.geekbrains.cookbook.domain.Category;
import ru.geekbrains.cookbook.repository.CategoryRepository;
import ru.geekbrains.cookbook.service.CategoryService;
import ru.geekbrains.cookbook.service.exception.CategoryCannotDeleteException;
import ru.geekbrains.cookbook.service.exception.CategoryNotFoundException;
import java.util.List;
import java.util.Optional;

@Service("categoryService")
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional
    public Category getCategoryById(Long id)  {
        return categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);
    }

    @Override
    @Transactional
    public Category saveCategory(Category category) {
        if(category.getId() != null){
            Optional<Category> optionalCategory = categoryRepository.findById(category.getId());
            if(!optionalCategory.isPresent())
                throw new CategoryNotFoundException(String.format("Category with ID=%d doesn't exist", category.getId()));
        }

        category = categoryRepository.save(category);
        return category;
    }

    @Override
    @Transactional
    public boolean removeCategory(Long id) {
        try{
            categoryRepository.deleteById(id);
            return true;
        }
        catch(EmptyResultDataAccessException e){
            CategoryNotFoundException exc = new CategoryNotFoundException(String.format("Category with ID=%d doesn't exist", id));
            exc.initCause(e);
            throw exc;
        }
        catch(DataIntegrityViolationException e){
            CategoryCannotDeleteException exc = new CategoryCannotDeleteException(String.format("Cannot remove the category with ID=%d cause it has linked recipes", id));
            exc.initCause(e);
            throw exc;
        }
    }

}

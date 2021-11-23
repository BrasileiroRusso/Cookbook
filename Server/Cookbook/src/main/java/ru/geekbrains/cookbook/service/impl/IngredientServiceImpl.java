package ru.geekbrains.cookbook.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.geekbrains.cookbook.domain.Ingredient;
import ru.geekbrains.cookbook.repository.IngredientRepository;
import ru.geekbrains.cookbook.service.IngredientService;
import ru.geekbrains.cookbook.service.exception.IngredientCannotDeleteException;
import ru.geekbrains.cookbook.service.exception.IngredientNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {
    private final IngredientRepository ingredientRepository;

    @Override
    public List<Ingredient> findAll() {
        return ingredientRepository.findAll();
    }

    @Override
    @Transactional
    public Ingredient getIngredientById(Long id)  {
        return ingredientRepository.findById(id).orElseThrow(IngredientNotFoundException::new);
    }

    @Override
    @Transactional
    public Ingredient saveIngredient(Ingredient ingredient) {
        if(ingredient.getId() != null){
            Optional<Ingredient> optionalIngredient = ingredientRepository.findById(ingredient.getId());
            if(!optionalIngredient.isPresent())
                throw new IngredientNotFoundException(String.format("Ingredient with ID=%d doesn't exist", ingredient.getId()));
        }

        ingredient = ingredientRepository.save(ingredient);
        return ingredient;
    }

    @Override
    @Transactional
    public boolean removeIngredient(Long id) {
        try{
            ingredientRepository.deleteById(id);
            return true;
        }
        catch(EmptyResultDataAccessException e){
            IngredientNotFoundException exc = new IngredientNotFoundException(String.format("Ingredient with ID=%d doesn't exist", id));
            exc.initCause(e);
            throw exc;
        }
        catch(DataIntegrityViolationException e){
            IngredientCannotDeleteException exc = new IngredientCannotDeleteException(String.format("Cannot remove the ingredient with ID=%d cause it has linked recipes", id));
            exc.initCause(e);
            throw exc;
        }
    }

}




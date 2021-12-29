package ru.geekbrains.cookbook.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.geekbrains.cookbook.domain.Ingredient;
import ru.geekbrains.cookbook.domain.file.LinkedFiles;
import ru.geekbrains.cookbook.dto.IngredientDto;
import ru.geekbrains.cookbook.mapper.IngredientMapper;
import ru.geekbrains.cookbook.repository.IngredientRepository;
import ru.geekbrains.cookbook.service.IngredientService;
import ru.geekbrains.cookbook.service.UploadFileService;
import ru.geekbrains.cookbook.service.exception.ResourceCannotDeleteException;
import ru.geekbrains.cookbook.service.exception.ResourceNotFoundException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {
    private final IngredientRepository ingredientRepository;
    private final UploadFileService uploadFileService;

    @Override
    @Transactional
    public List<IngredientDto> findAll() {
        List<Ingredient> ingredients = ingredientRepository.findAll();
        List<IngredientDto> ingredientDtos = IngredientMapper.ingredientListToDtoList(ingredients);
        Map<Long, String> imageMap = uploadFileService.getUploadedFilesByObjectType(Ingredient.class);
        ingredientDtos.forEach(r -> r.setImagePath(imageMap.getOrDefault(r.getId(), "")));
        return ingredientDtos;
    }

    @Override
    @Transactional
    public IngredientDto getIngredientById(Long ingredientId)  {
        IngredientDto ingredientDto = IngredientMapper.ingredientToDto(findIngredientById(ingredientId));
        String filename = uploadFileService.getFirstUploadedFile(ingredientId, Ingredient.class);
        ingredientDto.setImagePath(filename);
        return ingredientDto;
    }

    @Override
    @Transactional
    public IngredientDto saveIngredient(IngredientDto ingredientDto) {
        Ingredient ingredient;
        if(ingredientDto.getId() != null)
            ingredient = findIngredientById(ingredientDto.getId());
        else
            ingredient = new Ingredient();
        ingredient.setBriefName(ingredientDto.getBriefName());
        ingredient.setName(ingredientDto.getName());
        ingredient.setGroup(ingredientDto.getGroup());
        if(ingredientDto.getParentId() != null){
            Ingredient parentIngredient = findIngredientById(ingredientDto.getParentId());
            ingredient.setParentIngredient(parentIngredient);
        }
        else{
            ingredient.setParentIngredient(null);
        }
        ingredient = ingredientRepository.save(ingredient);
        return IngredientMapper.ingredientToDto(ingredient);
    }

    @Override
    @Transactional
    public boolean removeIngredient(Long id) {
        try{
            Ingredient ingredient = findIngredientById(id);
            ingredientRepository.delete(ingredient);
            return true;
        }
        catch(DataIntegrityViolationException e){
            ResourceCannotDeleteException exc = new ResourceCannotDeleteException();
            exc.initCause(e);
            throw exc;
        }
    }

    private Ingredient findIngredientById(Long id){
        return ingredientRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

}




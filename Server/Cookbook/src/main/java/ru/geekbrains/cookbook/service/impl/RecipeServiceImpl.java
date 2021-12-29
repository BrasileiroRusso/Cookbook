package ru.geekbrains.cookbook.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.geekbrains.cookbook.domain.HashTag;
import ru.geekbrains.cookbook.domain.Recipe;
import ru.geekbrains.cookbook.domain.RecipeIngredient;
import ru.geekbrains.cookbook.dto.RecipeDto;
import ru.geekbrains.cookbook.event.NewRecipeEvent;
import ru.geekbrains.cookbook.mapper.RecipeMapper;
import ru.geekbrains.cookbook.repository.RecipeIngredientRepository;
import ru.geekbrains.cookbook.repository.RecipeRepository;
import ru.geekbrains.cookbook.repository.specification.RecipeSpecification;
import ru.geekbrains.cookbook.service.RecipeService;
import ru.geekbrains.cookbook.service.TagService;
import ru.geekbrains.cookbook.service.UploadFileService;
import ru.geekbrains.cookbook.service.exception.ResourceNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service("recipeService")
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final UploadFileService uploadFileService;
    private final TagService tagService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public List<RecipeDto> findAll() {
        return recipeRepository.findAll().stream()
                .map(RecipeMapper::recipeToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Page<RecipeDto> findAll(Pageable pageable, Long categoryId, String titleRegex, Integer prepareTime, List<String> tags, Long authorId) {
        Page<Recipe> page = recipeRepository.findAll(RecipeSpecification.recipeFilter(categoryId, titleRegex, prepareTime, tags, authorId), pageable);
        Page<RecipeDto> pageDto = page.map(RecipeMapper::recipeToDto);
        List<Long> recipeIds = pageDto.getContent().stream().map(RecipeDto::getId).collect(Collectors.toList());
        Map<Long, String> imageMap = uploadFileService.getUploadedFilesByObjectList(recipeIds, Recipe.class);
        pageDto.getContent().forEach(r -> r.setImagePath(imageMap.getOrDefault(r.getId(), "")));
        return pageDto;
    }

    @Override
    @Transactional
    public RecipeDto getRecipeById(Long recipeId) {
        Recipe recipe = findRecipeById(recipeId);
        RecipeDto recipeDto = RecipeMapper.recipeToDto(recipe);
        String filename = uploadFileService.getFirstUploadedFile(recipeId, Recipe.class);
        recipeDto.setImagePath(filename);
        return recipeDto;
    }

    @Override
    @Transactional
    public RecipeDto saveRecipe(RecipeDto recipeDto) {
        Recipe recipe = RecipeMapper.DtoToRecipe(recipeDto);
        Recipe newRecipe = recipe;
        Set<RecipeIngredient> ingredients = recipe.getIngredients();
        Long recipeId;

        if(recipe.getId() != null){
            recipeId = recipe.getId();
            newRecipe = findRecipeById(recipe.getId());
            newRecipe.setCategory(recipe.getCategory());
            newRecipe.setDescription(recipe.getDescription());
            newRecipe.setTitle(recipe.getTitle());
            newRecipe.setUser(recipe.getUser());
            newRecipe.setSteps(recipe.getSteps());
            newRecipe.setTags(recipe.getTags());
            newRecipe.setPrepareTime(recipe.getPrepareTime());
            newRecipe.setComment(recipe.getComment());
            Set<RecipeIngredient.Id> ids = new HashSet<>();
            if(ingredients != null){
                ingredients.forEach(i -> {
                    i.getId().setRecipeId(recipeId);
                    i.getId().setIngredientId(i.getIngredient().getId());
                    ids.add(i.getId());
                });
                recipeIngredientRepository.saveAll(ingredients);
            }
            if(newRecipe.getIngredients() != null){
                for(RecipeIngredient recipeIngredient: newRecipe.getIngredients()){
                    if(!ids.contains(recipeIngredient.getId()))
                        recipeIngredientRepository.deleteById(recipeIngredient.getId());
                }
            }
            newRecipe.setIngredients(ingredients);
            newRecipe = recipeRepository.save(newRecipe);
        }
        else{
            newRecipe.setIngredients(null);
            newRecipe = recipeRepository.save(newRecipe);
            recipeId = newRecipe.getId();
            if(ingredients != null){
                ingredients.forEach(i -> {
                    i.getId().setRecipeId(recipeId);
                    i.getId().setIngredientId(i.getIngredient().getId());
                });
                recipeIngredientRepository.saveAll(ingredients);
            }
            eventPublisher.publishEvent(new NewRecipeEvent(newRecipe));
        }

        return RecipeMapper.recipeToDto(newRecipe);
    }

    @Override
    @Transactional
    public boolean removeRecipe(Long recipeId) {
        Recipe recipe = findRecipeById(recipeId);
        recipeRepository.delete(recipe);
        return true;
    }

    @Override
    @Transactional
    public Set<HashTag> getTagsById(Long recipeId) {
        Recipe recipe = findRecipeById(recipeId);
        return recipe.getTags();
    }

    @Override
    @Transactional
    public boolean addTagToRecipe(Long recipeId, Long tagId) {
        Recipe recipe = findRecipeById(recipeId);
        HashTag tag = tagService.getTagById(tagId);
        recipe.getTags().add(tag);
        recipeRepository.save(recipe);
        return true;
    }

    @Override
    @Transactional
    public boolean removeTagFromRecipe(Long recipeId, Long tagId) {
        Recipe recipe = findRecipeById(recipeId);
        HashTag tag = tagService.getTagById(tagId);
        recipe.getTags().remove(tag);
        recipeRepository.save(recipe);
        return true;
    }

    private Recipe findRecipeById(Long recipeId){
        return recipeRepository.findById(recipeId).orElseThrow(ResourceNotFoundException::new);
    }
}

package ru.geekbrains.cookbook.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.geekbrains.cookbook.domain.RecipeRating;
import ru.geekbrains.cookbook.domain.UserRating;
import ru.geekbrains.cookbook.repository.RecipeRatingRepository;
import ru.geekbrains.cookbook.repository.UserRatingRepository;
import ru.geekbrains.cookbook.service.RatingService;
import ru.geekbrains.cookbook.service.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {
    private final UserRatingRepository userRatingRepository;
    private final RecipeRatingRepository recipeRatingRepository;

    @Override
    @Transactional
    public List<UserRating> getAllRecipeRatings(Long recipeId) {
        return userRatingRepository.findAllByRecipe_Id(recipeId);
    }

    @Override
    @Transactional
    public UserRating getRecipeRatingByUserId(Long recipeId, Long userId) {
        UserRating.Id id = new UserRating.Id(recipeId, userId);
        UserRating userRating = userRatingRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        return userRating;
    }

    @Override
    @Transactional
    public UserRating saveRating(Long recipeId, Long userId, int rate) {
        UserRating.Id id = new UserRating.Id(recipeId, userId);
        UserRating userRating;
        RecipeRating recipeRating;
        Optional<UserRating> optionalRating = userRatingRepository.findById(id);
        Optional<RecipeRating> optionalRecipeRating = recipeRatingRepository.findById(recipeId);
        if(optionalRecipeRating.isPresent()){
            recipeRating = optionalRecipeRating.get();
        }
        else{
            recipeRating = new RecipeRating(recipeId);
        }
        if(optionalRating.isPresent()){
            userRating = optionalRating.get();
            recipeRating.addToTotalRating(rate - userRating.getRate());
        }
        else{
            userRating = new UserRating();
            userRating.getId().setRecipeId(recipeId);
            userRating.getId().setUserId(userId);
            recipeRating.addToTotalRating(rate);
            recipeRating.incRatings();
        }
        userRating.setRate(rate);
        userRatingRepository.save(userRating);
        recipeRatingRepository.save(recipeRating);
        return userRating;
    }

    @Override
    @Transactional
    public void removeRating(Long recipeId, Long userId) {
        UserRating.Id id = new UserRating.Id(recipeId, userId);
        UserRating rating = userRatingRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        RecipeRating recipeRating = recipeRatingRepository.findById(recipeId).orElseThrow(IllegalStateException::new);
        recipeRating.decRatings();
        recipeRating.addToTotalRating(-rating.getRate());
        recipeRatingRepository.save(recipeRating);
        userRatingRepository.delete(rating);
    }

}

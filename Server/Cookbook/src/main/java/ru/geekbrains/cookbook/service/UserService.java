package ru.geekbrains.cookbook.service;

import ru.geekbrains.cookbook.auth.User;
import ru.geekbrains.cookbook.dto.RecipeDto;
import ru.geekbrains.cookbook.dto.UserDto;
import java.util.List;
import java.util.Set;

public interface UserService {
    List<User> getUserList();
    User findByUsername(String username);
    User getUser(Long id);
    User saveUser(UserDto userDto);
    Set<RecipeDto> getFavoriteRecipes(Long userId);
    void addRecipeToFavorites(Long userId, Long recipeId);
    void removeRecipeFromFavorites(Long userId, Long recipeId);
}

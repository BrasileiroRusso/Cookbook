package ru.geekbrains.cookbook.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import ru.geekbrains.cookbook.auth.Role;
import ru.geekbrains.cookbook.auth.User;
import ru.geekbrains.cookbook.domain.Recipe;
import ru.geekbrains.cookbook.dto.RecipeDto;
import ru.geekbrains.cookbook.mapper.RecipeMapper;
import ru.geekbrains.cookbook.repository.*;
import ru.geekbrains.cookbook.service.exception.*;

import java.util.*;
import java.util.stream.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> getUserList(){
        return userRepository.findAll();
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional
    public User getUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if(!optionalUser.isPresent())
            throw new ResourceNotFoundException(String.format("User with ID = %d doesn't exist", id));
        return optionalUser.get();
    }

    @Transactional
    public User saveUser(User user){
        user = userRepository.save(user);
        return user;
    }

    @Transactional
    public Set<RecipeDto> getFavoriteRecipes(Long userId){
        User user = userRepository.findById(userId).orElseThrow(ResourceNotFoundException::new);
        Set<RecipeDto> recipeSet = user.getFavoriteRecipes().stream().map(RecipeMapper::recipeToDto).collect(Collectors.toSet());
        return recipeSet;
    }

    @Transactional
    public void addRecipeToFavorites(Long userId, Long recipeId){
        User user = userRepository.findById(userId).orElseThrow(ResourceNotFoundException::new);
        Recipe recipe = new Recipe();
        recipe.setId(recipeId);
        user.getFavoriteRecipes().add(recipe);
        userRepository.save(user);
    }

    @Transactional
    public void removeRecipeFromFavorites(Long userId, Long recipeId){
        User user = userRepository.findById(userId).orElseThrow(ResourceNotFoundException::new);
        user.getFavoriteRecipes().removeIf(recipe -> recipe.getId().equals(recipeId));
        userRepository.save(user);
    }
}

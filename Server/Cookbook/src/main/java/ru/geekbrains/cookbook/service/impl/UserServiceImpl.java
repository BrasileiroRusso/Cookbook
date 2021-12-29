package ru.geekbrains.cookbook.service.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import ru.geekbrains.cookbook.auth.Role;
import ru.geekbrains.cookbook.auth.User;
import ru.geekbrains.cookbook.domain.Recipe;
import ru.geekbrains.cookbook.dto.RecipeDto;
import ru.geekbrains.cookbook.dto.UserDto;
import ru.geekbrains.cookbook.event.RegistrationCompletedEvent;
import ru.geekbrains.cookbook.mapper.RecipeMapper;
import ru.geekbrains.cookbook.repository.*;
import ru.geekbrains.cookbook.service.UserService;
import ru.geekbrains.cookbook.service.exception.*;
import java.util.*;
import java.util.stream.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public List<User> getUserList(){
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    @Transactional
    public User getUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if(!optionalUser.isPresent())
            throw new ResourceNotFoundException(String.format("User with ID = %d doesn't exist", id));
        return optionalUser.get();
    }

    @Override
    @Transactional
    public User saveUser(UserDto userDto){
        String username = userDto.getUsername();
        if(userRepository.existsByUsername(username))
            throw new UserAlreadyExistsException(username);
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(userDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Role userRole = roleRepository.getByName("ROLE_USER");
        newUser.setRoles(Collections.singletonList(userRole));
        newUser = userRepository.save(newUser);
        eventPublisher.publishEvent(new RegistrationCompletedEvent(newUser));
        return newUser;
    }

    @Override
    @Transactional
    public Set<RecipeDto> getFavoriteRecipes(Long userId){
        User user = userRepository.findById(userId).orElseThrow(ResourceNotFoundException::new);
        Set<RecipeDto> recipeSet = user.getFavoriteRecipes().stream().map(RecipeMapper::recipeToDto).collect(Collectors.toSet());
        return recipeSet;
    }

    @Override
    @Transactional
    public void addRecipeToFavorites(Long userId, Long recipeId){
        User user = userRepository.findById(userId).orElseThrow(ResourceNotFoundException::new);
        Recipe recipe = new Recipe();
        recipe.setId(recipeId);
        user.getFavoriteRecipes().add(recipe);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void removeRecipeFromFavorites(Long userId, Long recipeId){
        User user = userRepository.findById(userId).orElseThrow(ResourceNotFoundException::new);
        user.getFavoriteRecipes().removeIf(recipe -> recipe.getId().equals(recipeId));
        userRepository.save(user);
    }
}

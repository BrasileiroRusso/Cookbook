package ru.geekbrains.cookbook.controller.rest;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.geekbrains.cookbook.auth.User;
import ru.geekbrains.cookbook.service.UserService;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/rest/user")
public class UserController {
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers(){
        return userService.getUserList();
    }

    @GetMapping("/{user_id}")
    public User getUserByID(@PathVariable(value="user_id") Long userID) {
        return userService.getUser(userID);
    }
}

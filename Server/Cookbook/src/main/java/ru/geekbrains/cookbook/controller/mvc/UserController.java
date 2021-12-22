package ru.geekbrains.cookbook.controller.mvc;

import com.amazonaws.services.elasticache.model.UserAlreadyExistsException;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import ru.geekbrains.cookbook.auth.User;
import ru.geekbrains.cookbook.dto.UserDto;
import ru.geekbrains.cookbook.event.RegistrationCompletedEvent;
import ru.geekbrains.cookbook.service.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller("mvcUserController")
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
    private UserService userService;
    private ApplicationEventPublisher eventPublisher;

    @GetMapping("/registration")
    public String showRegistrationForm(WebRequest request, Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        return "registration/form";
    }

    @PostMapping("/registration")
    public ModelAndView registerUserAccount(@ModelAttribute("user") @Valid UserDto userDto,
                                            ModelAndView modelAndView,
                                            HttpServletRequest request,
                                            Errors errors) {
        try {
            User newUser = userService.saveUser(userDto);
            eventPublisher.publishEvent(new RegistrationCompletedEvent(newUser));
        } catch (UserAlreadyExistsException uaeEx) {
            modelAndView.addObject("message", "Пользователь с данным именем уже зарегистрирован!");
            return modelAndView;
        }
        return new ModelAndView("registration/success", "user", userDto);
    }
}

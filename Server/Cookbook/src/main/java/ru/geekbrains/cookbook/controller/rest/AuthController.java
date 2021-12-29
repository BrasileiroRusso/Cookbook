package ru.geekbrains.cookbook.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.cookbook.auth.User;
import ru.geekbrains.cookbook.dto.UserDto;
import ru.geekbrains.cookbook.event.RegistrationCompletedEvent;
import ru.geekbrains.cookbook.service.UserService;
import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Регистрация пользователей", description = "API для регистрации и аутентификации пользователей")
public class AuthController {
    private UserService userService;
    private ApplicationEventPublisher eventPublisher;

    @Operation(summary = "Регистрация нового пользователя", description = "Регистрирует нового пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно"),
            @ApiResponse(responseCode = "405", description = "Некорректные входные данные")
    })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/registration")
    public ResponseEntity<?> registerUserAccount(@Parameter(description = "Параметры новой учетной записи", required = true) @RequestBody @Valid UserDto userDto) {
        User newUser = userService.saveUser(userDto);
        eventPublisher.publishEvent(new RegistrationCompletedEvent(newUser));
        return ResponseEntity.ok().build();
    }

}

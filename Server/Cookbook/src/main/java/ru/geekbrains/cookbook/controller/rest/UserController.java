package ru.geekbrains.cookbook.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.cookbook.auth.User;
import ru.geekbrains.cookbook.service.UserService;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user")
@Tag(name = "Пользователи", description = "API для пользователей")
public class UserController {
    private UserService userService;

    @Operation(summary = "Возвращает список пользователей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = User.class)))}
            )}
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<User> getAllUsers(){
        return userService.getUserList();
    }

    @Operation(summary = "Возвращает пользователя по идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Указан некорректный идентификатор пользователя", content = @Content),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = @Content)
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{user_id}")
    public User getUserByID(@Parameter(description = "Идентификатор пользователя", required = true) @PathVariable(value="user_id") Long userID) {
        return userService.getUser(userID);
    }
}

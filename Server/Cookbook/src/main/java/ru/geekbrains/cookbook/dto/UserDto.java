package ru.geekbrains.cookbook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.geekbrains.cookbook.validation.constraint.PasswordMatches;
import ru.geekbrains.cookbook.validation.constraint.ValidEmail;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@PasswordMatches
public class UserDto {
    @NotNull
    @NotEmpty
    private String username;
    @NotNull
    @NotEmpty
    private String password;
    @NotNull
    private String matchingPassword;
    @NotNull
    @NotEmpty
    @ValidEmail
    private String email;
}

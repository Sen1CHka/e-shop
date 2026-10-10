package eshop.contracts;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 30, message = "Username must have 3-30 characters")
        @Pattern(regexp = "^[A-Za-z0-9._-]*$", message = "Username may contain only letters, digits, dot, dash and underscore")
        String username,

        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name is too long")
        String realName,

        @NotBlank(message = "E-mail is required")
        @Email(message = "E-mail is not valid")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 6, max = 100, message = "Password must have at least 6 characters")
        String password
) {
}

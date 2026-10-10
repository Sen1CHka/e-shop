package eshop.contracts;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProfileRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name is too long")
        String realName,

        @NotBlank(message = "E-mail is required")
        @Email(message = "E-mail is not valid")
        String email,

        @Size(max = 30, message = "Phone is too long")
        String phone
) {
}

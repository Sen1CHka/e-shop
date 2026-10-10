package eshop.controller;

import eshop.contracts.*;
import eshop.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication and profile")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a new customer", description = "Creates the account and returns a JWT so the user is signed in right away")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    @Operation(summary = "Sign in", description = "Returns a JWT to be sent in the Authorization: Bearer header")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return userService.login(request);
    }

    @GetMapping("/me")
    @Operation(summary = "Get the signed-in user")
    public UserResponse me(Authentication authentication) {
        return userService.getProfile(authentication.getName());
    }

    @PutMapping("/me")
    @Operation(summary = "Update the profile of the signed-in user")
    public UserResponse updateProfile(@Valid @RequestBody ProfileRequest request, Authentication authentication) {
        return userService.updateProfile(authentication.getName(), request);
    }

    @PutMapping("/me/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Change the password of the signed-in user")
    public void changePassword(@Valid @RequestBody PasswordChangeRequest request, Authentication authentication) {
        userService.changePassword(authentication.getName(), request);
    }
}

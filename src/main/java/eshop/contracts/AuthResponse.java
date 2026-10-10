package eshop.contracts;

public record AuthResponse(String token, UserResponse user) {
}

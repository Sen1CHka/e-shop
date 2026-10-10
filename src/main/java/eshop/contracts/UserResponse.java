package eshop.contracts;

import eshop.domain.Role;
import eshop.domain.User;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String username,
        String realName,
        String email,
        String phone,
        Role role,
        LocalDateTime createdAt
) {
    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getRealName(), user.getEmail(),
                user.getPhone(), user.getRole(), user.getCreatedAt());
    }
}

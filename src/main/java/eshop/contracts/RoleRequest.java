package eshop.contracts;

import eshop.domain.Role;
import jakarta.validation.constraints.NotNull;

public record RoleRequest(@NotNull(message = "Role is required") Role role) {
}

package eshop.contracts;

import eshop.domain.OrderState;
import jakarta.validation.constraints.NotNull;

public record OrderStateRequest(@NotNull(message = "State is required") OrderState state) {
}

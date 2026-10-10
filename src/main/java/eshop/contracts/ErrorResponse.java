package eshop.contracts;

import java.util.Map;

public record ErrorResponse(int status, String message, Map<String, String> fieldErrors) {

    public ErrorResponse(int status, String message) {
        this(status, message, Map.of());
    }
}

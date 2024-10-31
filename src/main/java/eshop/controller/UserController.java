package eshop.controller;

import eshop.contracts.User;
import eshop.contracts.UserEdit;
import eshop.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.StreamSupport;

@RestController
@CrossOrigin
@RequestMapping("/api/user")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieves a list of all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of users",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class)))
    })
    public ResponseEntity<?> getAllUsers() {
        List<eshop.domain.User> orders = StreamSupport.stream(userService.readAll().spliterator(), false)
                .toList();

        List<User> orderDTOs = orders.stream()
                .map(userService::convertUserToDto)
                .toList();
        return ResponseEntity.ok(orderDTOs);
    }

    @PostMapping
    @Operation(summary = "Create a user", description = "Creates a new user with the given details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful creation of the user",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class)))
    })
    public ResponseEntity<?> createUser(@RequestBody UserEdit userDto) {
        if(userService.readById(userDto.getUsername()).isPresent()) throw new RuntimeException("User with same username already exists");
        eshop.domain.User user = userService.convertDtoToUser(userDto);
        return ResponseEntity.ok(userService.create(user));
    }

    @PutMapping("/{username}")
    @Operation(summary = "Update a user", description = "Updates the user details for the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<?> updateUser(@PathVariable String username, @RequestBody UserEdit userDto) {
        if (userService.readById(username).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        eshop.domain.User user = userService.convertDtoToUser(userDto);
        return ResponseEntity.ok(userService.update(username, user));
    }

    @DeleteMapping("/{username}")
    @Operation(summary = "Delete a user", description = "Deletes a user with the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully deleted"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<?> deleteUser(@PathVariable  String username) {
        if (userService.readById(username).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userService.deleteById(username));
    }

}


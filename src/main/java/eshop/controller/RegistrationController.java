package eshop.controller;

import eshop.contracts.UserRequest;
import eshop.domain.User;
import eshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class RegistrationController {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRequest userRequest) {

        Optional<User> user = userService.findByUsername(userRequest.getUsername());

        if(user.isPresent()) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }
        userService.registerUser(userRequest);
        return ResponseEntity.ok("Registration OK");
    }

}

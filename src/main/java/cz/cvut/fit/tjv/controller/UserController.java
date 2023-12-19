package cz.cvut.fit.tjv.controller;

import cz.cvut.fit.tjv.domain.User;
import cz.cvut.fit.tjv.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Iterable<User> readAll() {
        return userService.readAll();
    }
    @PostMapping
    public User create(@RequestBody User user) {
        return userService.create(user);
    }
}


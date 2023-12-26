package cz.cvut.fit.tjv.controller;

import cz.cvut.fit.tjv.domain.User;
import cz.cvut.fit.tjv.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Iterable<User> readAll() {
        return userService.readAll();
    }

}


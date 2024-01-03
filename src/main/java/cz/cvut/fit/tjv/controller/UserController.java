package cz.cvut.fit.tjv.controller;

import cz.cvut.fit.tjv.contracts.OrderDTO;
import cz.cvut.fit.tjv.contracts.UserDTO;
import cz.cvut.fit.tjv.domain.Order;
import cz.cvut.fit.tjv.domain.User;
import cz.cvut.fit.tjv.service.OrderServiceImpl;
import cz.cvut.fit.tjv.service.UserService;
import cz.cvut.fit.tjv.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
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
    public ResponseEntity<List<UserDTO>> getAllOrders() {
        List<User> orders = StreamSupport.stream(userService.readAll().spliterator(), false)
                .toList();

        List<UserDTO> orderDTOs = orders.stream()
                .map(UserServiceImpl::convertUserToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderDTOs);
    }

}


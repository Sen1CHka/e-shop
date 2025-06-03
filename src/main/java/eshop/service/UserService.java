package eshop.service;

import eshop.contracts.UserRequest;
import eshop.contracts.UserResponse;
import eshop.domain.Role;
import eshop.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;


public interface UserService extends CrudService<User, Integer> , UserDetailsService {
    User convertDtoToUser(UserRequest userDto);
    UserResponse convertUserToDto(User user);
    void roleUpdate(User user, Role role);
    User registerUser(UserRequest userDto);
    //void registerUser(UserRequest userDto);
    Optional<User> findByUsername(String username);
}

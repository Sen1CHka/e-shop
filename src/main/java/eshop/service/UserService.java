package eshop.service;

import eshop.contracts.UserRequest;
import eshop.contracts.UserResponse;
import eshop.domain.Role;
import eshop.domain.User;

import java.util.Optional;


public interface UserService extends CrudService<User, Integer>{
    User convertDtoToUser(UserRequest userDto);
    UserResponse convertUserToDto(User user);
    void roleUpdate(User user, Role role);
    User registerUser(UserRequest userDto);
    //void registerUser(UserRequest userDto);
    Optional<User> findByUsername(String username);
}

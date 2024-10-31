package eshop.service;

import eshop.contracts.UserEdit;
import eshop.domain.User;


public interface UserService extends CrudService<User, String>{
    User convertDtoToUser(UserEdit userDto);
    eshop.contracts.User convertUserToDto(User user);
}

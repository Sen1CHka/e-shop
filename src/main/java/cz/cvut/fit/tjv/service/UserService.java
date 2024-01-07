package cz.cvut.fit.tjv.service;

import cz.cvut.fit.tjv.contracts.UserEdit;
import cz.cvut.fit.tjv.domain.User;
import org.springframework.stereotype.Component;


public interface UserService extends CrudService<User, String>{
    User convertDtoToUser(UserEdit userDto);
    cz.cvut.fit.tjv.contracts.User convertUserToDto(cz.cvut.fit.tjv.domain.User user);
}

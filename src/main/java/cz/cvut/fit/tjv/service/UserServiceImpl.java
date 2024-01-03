package cz.cvut.fit.tjv.service;

import cz.cvut.fit.tjv.contracts.UserDTO;
import cz.cvut.fit.tjv.domain.User;
import cz.cvut.fit.tjv.repositary.UserRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl extends CrudServiceImpl<User, String> implements UserService{


    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected CrudRepository<User, String> getRepository() {
        return userRepository;
    }

    public static UserDTO convertUserToDto(User user)
    {
        if(user == null) return new UserDTO();
        return new UserDTO(user.getUsername(), user.getRealName(), user.getEmail());
    }
}

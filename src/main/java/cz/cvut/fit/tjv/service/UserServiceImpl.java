package cz.cvut.fit.tjv.service;

import cz.cvut.fit.tjv.contracts.User;
import cz.cvut.fit.tjv.repository.UserRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl extends CrudServiceImpl<cz.cvut.fit.tjv.domain.User, String> implements UserService{


    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected CrudRepository<cz.cvut.fit.tjv.domain.User, String> getRepository() {
        return userRepository;
    }

    public static User convertUserToDto(cz.cvut.fit.tjv.domain.User user)
    {
        if(user == null) return new User();
        return new User(user.getUsername(), user.getRealName(), user.getEmail());
    }

    @Override
    public cz.cvut.fit.tjv.domain.User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}

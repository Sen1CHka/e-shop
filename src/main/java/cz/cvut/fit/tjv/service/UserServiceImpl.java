package cz.cvut.fit.tjv.service;

import cz.cvut.fit.tjv.contracts.User;
import cz.cvut.fit.tjv.contracts.UserEdit;
import cz.cvut.fit.tjv.domain.Order;
import cz.cvut.fit.tjv.repository.OrderRepository;
import cz.cvut.fit.tjv.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl extends CrudServiceImpl<cz.cvut.fit.tjv.domain.User, String> implements UserService{


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected CrudRepository<cz.cvut.fit.tjv.domain.User, String> getRepository() {
        return userRepository;
    }

    @Override
    public User convertUserToDto(cz.cvut.fit.tjv.domain.User user)
    {
        if(user == null) return new User();
        return new User(user.getUsername(), user.getRealName(), user.getEmail());
    }

    @Override
    public cz.cvut.fit.tjv.domain.User convertDtoToUser(UserEdit userDto) {
        cz.cvut.fit.tjv.domain.User user = new cz.cvut.fit.tjv.domain.User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setRealName(userDto.getRealName());
        return user;
    }

    @Override
    public cz.cvut.fit.tjv.domain.User update(String id, cz.cvut.fit.tjv.domain.User e) {
        if(userRepository.findById(id).isEmpty()) throw new RuntimeException("User doesnt exist");

        cz.cvut.fit.tjv.domain.User user = userRepository.findById(id).get();
        user.setRealName(e.getRealName());
        user.setPassword(e.getPassword());
        user.setEmail(e.getEmail());
        userRepository.save(user);

        return user;
    }

    @Override
    public String deleteById(String username) {
        if(!userRepository.existsById(username)) throw new RuntimeException("User doesnt exist");

        List<Order> orders = new ArrayList<>(userRepository.findById(username).get().getOrders());
        orderRepository.deleteAll(orders);
        userRepository.deleteById(username);

        return username;
    }
}

package eshop.service;

import eshop.contracts.User;
import eshop.contracts.UserEdit;
import eshop.domain.Order;
import eshop.repository.OrderRepository;
import eshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl extends CrudServiceImpl<eshop.domain.User, String> implements UserService{


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected CrudRepository<eshop.domain.User, String> getRepository() {
        return userRepository;
    }

    @Override
    public User convertUserToDto(eshop.domain.User user)
    {
        if(user == null) return new User();
        return new User(user.getUsername(), user.getRealName(), user.getEmail());
    }

    @Override
    public eshop.domain.User convertDtoToUser(UserEdit userDto) {
        eshop.domain.User user = new eshop.domain.User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setRealName(userDto.getRealName());
        return user;
    }

    @Override
    public eshop.domain.User update(String id, eshop.domain.User e) {
        if(userRepository.findById(id).isEmpty()) throw new RuntimeException("User doesnt exist");

        eshop.domain.User user = userRepository.findById(id).get();
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

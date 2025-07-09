package eshop.service;

import eshop.contracts.UserResponse;
import eshop.contracts.UserRequest;
import eshop.domain.Order;
import eshop.domain.Role;
import eshop.domain.User;
import eshop.repository.OrderRepository;
import eshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl extends CrudServiceImpl<User, Integer> implements UserService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, OrderRepository orderRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected CrudRepository<User, Integer> getRepository() {
        return userRepository;
    }

    @Override
    public UserResponse convertUserToDto(eshop.domain.User user)
    {
        if(user == null) return new UserResponse();
        return new UserResponse(user.getUsername(), user.getRealName(), user.getEmail(), user.getRole());
    }

    @Override
    public void roleUpdate(User user, Role role) {
        user.setRole(role);
        userRepository.save(user);
    }

    @Override
    public User registerUser(UserRequest userDto) {
        User newUser = convertDtoToUser(userDto);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        userRepository.save(newUser);
        return newUser;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User convertDtoToUser(UserRequest userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setRealName(userDto.getRealName());
        user.setRole(Role.USER);
        return user;
    }

    @Override
    public User update(Integer id, User e) {
        if(userRepository.findById(id).isEmpty()) throw new RuntimeException("User doesnt exist");

        eshop.domain.User user = userRepository.findById(id).get();
        user.setRealName(e.getRealName());
        user.setPassword(e.getPassword());
        user.setEmail(e.getEmail());
        userRepository.save(user);

        return user;
    }

    @Override
    public Integer deleteById(Integer id) {
        if(!userRepository.existsById(id)) throw new RuntimeException("User doesnt exist");

        List<Order> orders = new ArrayList<>(userRepository.findById(id).get().getOrders());
        orderRepository.deleteAll(orders);
        userRepository.deleteById(id);

        return id;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }

}

package eshop.service;

import eshop.contracts.*;
import eshop.domain.Role;
import eshop.domain.User;
import eshop.exception.ApiException;
import eshop.repository.AddressRepository;
import eshop.repository.OrderRepository;
import eshop.repository.ReviewRepository;
import eshop.repository.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, OrderRepository orderRepository,
                       ReviewRepository reviewRepository, AddressRepository addressRepository,
                       PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.reviewRepository = reviewRepository;
        this.addressRepository = addressRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw ApiException.conflict("Username is already taken");
        }
        if (userRepository.existsByEmailIgnoreCase(request.email())) {
            throw ApiException.conflict("An account with this e-mail already exists");
        }
        User user = new User();
        user.setUsername(request.username());
        user.setRealName(request.realName().trim());
        user.setEmail(request.email().trim());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);
        userRepository.save(user);
        return new AuthResponse(jwtUtil.generateToken(user.getUsername()), UserResponse.from(user));
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        User user = getByUsername(request.username());
        return new AuthResponse(jwtUtil.generateToken(user.getUsername()), UserResponse.from(user));
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> ApiException.notFound("User not found"));
    }

    public UserResponse getProfile(String username) {
        return UserResponse.from(getByUsername(username));
    }

    @Transactional
    public UserResponse updateProfile(String username, ProfileRequest request) {
        User user = getByUsername(username);
        String email = request.email().trim();
        if (!email.equalsIgnoreCase(user.getEmail()) && userRepository.existsByEmailIgnoreCase(email)) {
            throw ApiException.conflict("An account with this e-mail already exists");
        }
        user.setRealName(request.realName().trim());
        user.setEmail(email);
        user.setPhone(request.phone());
        return UserResponse.from(user);
    }

    @Transactional
    public void changePassword(String username, PasswordChangeRequest request) {
        User user = getByUsername(username);
        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw ApiException.badRequest("Current password is not correct");
        }
        user.setPassword(passwordEncoder.encode(request.newPassword()));
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll(Sort.by("id")).stream()
                .map(UserResponse::from)
                .toList();
    }

    @Transactional
    public UserResponse changeRole(Long id, Role role, String currentUsername) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("User not found"));
        if (user.getUsername().equals(currentUsername)) {
            throw ApiException.badRequest("You cannot change your own role");
        }
        user.setRole(role);
        return UserResponse.from(user);
    }

    @Transactional
    public void delete(Long id, String currentUsername) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("User not found"));
        if (user.getUsername().equals(currentUsername)) {
            throw ApiException.badRequest("You cannot delete your own account");
        }
        reviewRepository.deleteByUserId(id);
        addressRepository.deleteByUserId(id);
        orderRepository.deleteByUserId(id);
        userRepository.delete(user);
    }
}

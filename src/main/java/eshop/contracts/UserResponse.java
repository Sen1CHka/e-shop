package eshop.contracts;

import eshop.domain.Role;

import java.util.HashSet;
import java.util.Set;

public class UserResponse {

    private String username;
    private String realName;
    private String email;
    private Role role;

    public UserResponse() {}

    public UserResponse(String username, String realName, String email, Role role) {
        this.username = username;
        this.realName = realName;
        this.email = email;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealName() {
        return realName;
    }

    public Role getRole() {
        return role;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}

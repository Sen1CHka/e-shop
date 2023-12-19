package cz.cvut.fit.tjv.domain;

import jakarta.persistence.*;

import java.util.Collection;

@Entity
@Table(name = "users")
public class User implements EntityWithId<String>{
    @Id
    private String username = "";
    private String realName;
    private String email;
    private String password;
    @OneToMany(mappedBy = "client")
    private Collection<Order> orders;

    @Override
    public String getId() {
        return username;
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

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<Order> getOrders() {
        return orders;
    }

}

package cz.cvut.fit.tjv.contracts;

public class UserEdit {
    private String username;

    private String realName;

    private String email;

    private String password;

    public UserEdit() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserEdit(String username, String realName, String email, String password) {
        this.username = username;
        this.realName = realName;
        this.email = email;
        this.password = password;
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
}

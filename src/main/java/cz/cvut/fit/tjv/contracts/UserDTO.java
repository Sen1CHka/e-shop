package cz.cvut.fit.tjv.contracts;

public class UserDTO {

    private String username;
    private String realName;
    private String email;

    public UserDTO() {
    }

    public UserDTO(String username, String realName, String email) {
        this.username = username;
        this.realName = realName;
        this.email = email;
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

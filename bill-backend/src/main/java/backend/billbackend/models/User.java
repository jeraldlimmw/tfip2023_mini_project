package backend.billbackend.models;

import java.io.Serializable;

public class User implements Serializable{
    private Long userId;
    private String username;
    private String firstName;
    
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    @Override
    public String toString() {
        return "User [userId=" + userId + ", username=" + username + ", firstName=" + firstName + "]";
    }
}

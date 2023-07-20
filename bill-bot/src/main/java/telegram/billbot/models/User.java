package telegram.billbot.models;

public class User {
    private Long userId;
    private String firstName;
    private String username;

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    @Override
    public String toString() {
        return "BillCreator [userId=" + userId + ", firstName=" + firstName + ", username=" + username + "]";
    }    
}

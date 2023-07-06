package telegram.billbot.models;

public class BillCreator {
    private Long userId;
    private String firstname;
    private String username;

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getFirstname() {
        return firstname;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    @Override
    public String toString() {
        return "BillCreator [userId=" + userId + ", firstname=" + firstname + ", username=" + username + "]";
    }    
}

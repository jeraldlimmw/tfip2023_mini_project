package backend.billbackend.models;

public class StartData {
    Long chatId;
    User user;

    public Long getChatId() {
        return chatId;
    }
    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    @Override
    public String toString() {
        return "StartData [chatId=" + chatId + ", user=" + user + "]";
    }
}

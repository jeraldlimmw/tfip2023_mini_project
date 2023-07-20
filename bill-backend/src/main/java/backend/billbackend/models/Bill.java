package backend.billbackend.models;

import java.io.Serializable;
import java.util.List;

public class Bill implements Serializable{
    private String billId = "not set";
    private String chatId;
    private User user;
    private String title;
    private Double total;
    private List<String> friends;
    private List<Paid> paid;
    private Boolean byPercentage;
    private List<Item> items;
    private Double service;
    private Double tax;
    private long timestamp;
    
    public String getBillId() { return billId; }
    public void setBillID(String billId) { this.billId = billId; }

    public String getChatId() { return chatId; }
    public void setChatId(String chatId) { this.chatId = chatId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public List<String> getFriends() { return friends; }
    public void setFriends(List<String> friends) { this.friends = friends; }
    
    public List<Paid> getPaid() { return paid; }
    public void setPaid(List<Paid> paid) { this.paid = paid; }

    public Boolean getByPercentage() { return byPercentage; }
    public void setByPercentage(Boolean byPercentage) { this.byPercentage = byPercentage; }

    public List<Item> getItems() { return items; }
    public void setItems(List<Item> items) { this.items = items; }

    public Double getService() { return service; }
    public void setService(Double service) { this.service = service; }

    public Double getTax() { return tax; }
    public void setTax(Double tax) { this.tax = tax; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    
    @Override
    public String toString() {
        return "Bill [billId=" + billId + ", chatId=" + chatId + ", user=" + user + ", title=" + title + ", total="
                + total + ", friends=" + friends + ", paid=" + paid + ", byPercentage=" + byPercentage + ", items="
                + items + ", service=" + service + ", tax=" + tax + ", timestamp=" + timestamp + "]";
    }
}

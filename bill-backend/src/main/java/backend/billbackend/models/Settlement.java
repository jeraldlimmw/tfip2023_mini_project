package backend.billbackend.models;

import java.util.List;

public class Settlement {
    private String billId;
    private String title;
    private List<Transaction> transactions;
    private long timestamp;

    public Settlement() {
    }

    public Settlement(String billId) {
        this.billId = billId;
    }
    
    public String getBillId() {
        return billId;
    }
    public void setBillId(String billId) {
        this.billId = billId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public List<Transaction> getTransactions() {
        return transactions;
    }
    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Settlement [billId=" + billId + ", title=" + title + ", transactions=" + transactions
                + ", timestamp=" + timestamp + "]";
    }    
}
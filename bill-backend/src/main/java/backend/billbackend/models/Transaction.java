package backend.billbackend.models;

public class Transaction {
    private String id;
    private String billId;
    private String payer;
    private String payee;
    private Double amount;
    private String paylahQR;

    public Transaction() {
    }

    public Transaction(String id, String billId, String payer, String payee, Double amount) {
        this.id = id;
        this.billId = billId;
        this.payer = payer;
        this.payee = payee;
        this.amount = amount;
        //this.paylahQR = "";
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getBillId() {
        return billId;
    }
    public void setBillId(String billId) {
        this.billId = billId;
    }
    public String getPayer() {
        return payer;
    }
    public void setPayer(String payer) {
        this.payer = payer;
    }
    public String getPayee() {
        return payee;
    }
    public void setPayee(String payee) {
        this.payee = payee;
    }
    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Transaction [id=" + id + ", billId=" + billId + ", payer=" + payer + ", payee=" + payee + ", amount=" + amount + "]";
    } 
}

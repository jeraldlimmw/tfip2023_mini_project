package backend.billbackend.models;

import java.io.Serializable;

public class Paid implements Serializable{
    private String name;
    private Double amount;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    
    @Override
    public String toString() {
        return "Paid [name=" + name + ", amount=" + amount + "]";
    }
}

package backend.billbackend.models;

import java.io.Serializable;

public class Participant implements Serializable{
    private String name;
    private Double balance;
    private Double expense;

    public Participant() {
    }

    public Participant(String name, Double balance) {
        this.name = name;
        this.balance = balance;
        this.expense = 0.0;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Double getBalance() {
        return balance;
    }
    public void setBalance(Double balance) {
        this.balance = balance;
    }
    public Double getExpense() {
        return expense;
    }
    public void setExpense(Double expense) {
        this.expense = expense;
    }
    
    @Override
    public String toString() {
        return "Participant [name=" + name + ", balance=" + balance + ", expense=" + expense + "]";
    }
}

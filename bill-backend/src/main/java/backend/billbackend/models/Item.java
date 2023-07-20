package backend.billbackend.models;

import java.io.Serializable;
import java.util.List;

import jakarta.json.JsonObject;

public class Item implements Serializable{
    private String itemName;
    private Double price;
    private Integer quantity;
    private List<Double> shares;
    private List<Double> percentShares;
    
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    public List<Double> getShares() {
        return shares;
    }
    public void setShares(List<Double> shares) {
        this.shares = shares;
    }
    public List<Double> getPercentShares() {
        return percentShares;
    }
    public void setPercentShares(List<Double> percentShares) {
        this.percentShares = percentShares;
    }

    @Override
    public String toString() {
        return "Item [itemName=" + itemName + ", price=" + price + ", quantity=" 
                + quantity + ", shares=" + shares
                + ", percentShares=" + percentShares + "]";
    }

    public static Item createReceiptItem(JsonObject jo) {
        Item i = new Item();
        i.setItemName(jo.getString("description"));
        i.setPrice((jo.getJsonNumber("amount").bigDecimalValue()).doubleValue());
        i.setQuantity(1);
        return i;
    }
}

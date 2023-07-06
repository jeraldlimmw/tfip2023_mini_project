package backend.billbackend.models;

import java.util.List;

public class Item {
    private String itemName;
    private Double price;
    private Integer quantity;
    private List<Integer> share;
    private List<Double> percentShare;
    // private List<String> people;
    
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
    public List<Integer> getShare() {
        return share;
    }
    public void setShare(List<Integer> share) {
        this.share = share;
    }
    public List<Double> getPercentShare() {
        return percentShare;
    }
    public void setPercentShare(List<Double> percentShare) {
        this.percentShare = percentShare;
    }

    @Override
    public String toString() {
        return "Item [itemName=" + itemName + ", price=" + price + ", quantity=" + quantity + ", share=" + share
                + ", percentShare=" + percentShare + "]";
    }
}

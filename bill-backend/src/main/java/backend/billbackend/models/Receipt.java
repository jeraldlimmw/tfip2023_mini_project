package backend.billbackend.models;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Receipt {
    private List<Item> receiptItems;
    private Double receiptTotal;
    private Boolean taxAndServiceIncl = false;

    public Receipt() {
    }
    
    public Receipt(List<Item> receiptItems, Double receiptTotal) {
        this.receiptItems = receiptItems;
        this.receiptTotal = receiptTotal;
    }

    public List<Item> getReceiptItems() {
        return receiptItems;
    }
    public void setReceiptItems(List<Item> receiptItems) {
        this.receiptItems = receiptItems;
    }
    public Double getReceiptTotal() {
        return receiptTotal;
    }
    public void setReceiptTotal(Double receiptTotal) {
        this.receiptTotal = receiptTotal;
    }
    public Boolean getTaxAndServiceIncl() {
        return taxAndServiceIncl;
    }
    public void setTaxAndServiceIncl(Boolean taxAndServiceIncl) {
        this.taxAndServiceIncl = taxAndServiceIncl;
    }
    
    @Override
    public String toString() {
        return "Receipt [receiptItems=" + receiptItems + ", receiptTotal=" + receiptTotal + ", taxAndServiceIncl="
                + taxAndServiceIncl + "]";
    }

    public static Receipt create(String json) {
        List<Item> itemList = new LinkedList<>();
        Double sumOfAmount = 0.0;
        Double sumOfTotal = 0.0;
        
        InputStream is = new ByteArrayInputStream(json.getBytes());
        JsonReader reader = Json.createReader(is);
        JsonObject receipts = reader.readObject();
        JsonArray receiptArr = receipts.getJsonArray("receipts");

        for (int i = 0; i < receiptArr.size(); i++) {
            JsonObject jsonReceipt = receiptArr.get(i).asJsonObject();
            
            JsonArray itemArr = jsonReceipt.getJsonArray("items");
            for (int j = 0; j < itemArr.size(); j++) {
                JsonObject jsonItem = itemArr.get(j).asJsonObject();
                Item item = Item.createReceiptItem(jsonItem);
                sumOfAmount += item.getPrice();
                itemList.add(item);
            }
            sumOfTotal += jsonReceipt.getJsonNumber("total").bigDecimalValue().doubleValue();
        }    
        Receipt r = new Receipt(itemList, sumOfTotal);
        if (sumOfAmount == r.getReceiptTotal()) {
            r.setTaxAndServiceIncl(true);
        }
        return r;
    }
}

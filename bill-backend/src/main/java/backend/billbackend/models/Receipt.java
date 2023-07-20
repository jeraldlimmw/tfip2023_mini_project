package backend.billbackend.models;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Receipt implements Serializable{
    private List<Item> receiptItems;

    public Receipt() {
    }
    
    public Receipt(List<Item> receiptItems) {
        this.receiptItems = receiptItems;
    }

    public List<Item> getReceiptItems() {
        return receiptItems;
    }
    public void setReceiptItems(List<Item> receiptItems) {
        this.receiptItems = receiptItems;
    }

    @Override
    public String toString() {
        return "Receipt [receiptItems=" + receiptItems + "]";
    }

    public static Receipt create(String json) {
        List<Item> itemList = new LinkedList<>();
        
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
                itemList.add(item);
            }
        }    
        Receipt r = new Receipt(itemList);

        return r;
    }
}

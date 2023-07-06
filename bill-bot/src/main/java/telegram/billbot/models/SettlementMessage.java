package telegram.billbot.models;

public class SettlementMessage {
    private BillCreator billCreator;
    private Long chatId;
    private String text;

    public BillCreator getBillCreator() {
        return billCreator;
    }
    public void setBillCreator(BillCreator billCreator) {
        this.billCreator = billCreator;
    }
    public Long getChatId() {
        return chatId;
    }
    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    @Override
    public String toString() {
        return "SettlementMessage [billCreator=" + billCreator + ", chatId=" + chatId + ", text=" + text + "]";
    }
}

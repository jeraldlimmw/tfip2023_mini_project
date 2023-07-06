package telegram.billbot.inlinemarkups;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class InlineMarkup {

    public InlineKeyboardMarkup createStartMenu() {
        InlineKeyboardMarkup ikm = new InlineKeyboardMarkup();
        InlineKeyboardButton ikb = new InlineKeyboardButton("Split bill");
        ikb.setCallbackData("split bill");
        ikb.setUrl("eatnsplit.com");
        List<InlineKeyboardButton> buttonRow = new ArrayList<>();
        buttonRow.add(ikb);
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(buttonRow);
        ikm.setKeyboard(keyboard);
        return ikm;
    }

    public InlineKeyboardMarkup paymentMenu() {
        InlineKeyboardMarkup ikm = new InlineKeyboardMarkup();
        InlineKeyboardButton ikb = new InlineKeyboardButton("Paid");
        ikb.setCallbackData("paid");
        List<InlineKeyboardButton> buttonRow = new ArrayList<>();
        buttonRow.add(ikb);
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(buttonRow);
        ikm.setKeyboard(keyboard);
        return ikm;
    }

    public InlineKeyboardMarkup receivedMenu() {
        InlineKeyboardMarkup ikm = new InlineKeyboardMarkup();
        InlineKeyboardButton ikb = new InlineKeyboardButton("Received");
        ikb.setCallbackData("received");
        List<InlineKeyboardButton> buttonRow = new ArrayList<>();
        buttonRow.add(ikb);
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(buttonRow);
        ikm.setKeyboard(keyboard);
        return ikm;
    }
}

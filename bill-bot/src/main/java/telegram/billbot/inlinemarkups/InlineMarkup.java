package telegram.billbot.inlinemarkups;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import telegram.billbot.models.User;

public class InlineMarkup {

    public InlineKeyboardMarkup createSplitButton(Long chatId, User creator) {
        InlineKeyboardMarkup ikm = new InlineKeyboardMarkup();
        InlineKeyboardButton ikb = new InlineKeyboardButton("Split bill");
        ikb.setCallbackData("split bill");
        String url = "https://billbuddyv1-production.up.railway.app/#/start?"
                    + String.format("chat_id=%s&", chatId.toString())
                    + String.format("user_id=%s&", creator.getUserId().toString())
                    + String.format("username=%s&", creator.getUsername())
                    + String.format("firstName=%s", creator.getFirstName());
        System.out.println(url);
        ikb.setUrl(url);
        
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

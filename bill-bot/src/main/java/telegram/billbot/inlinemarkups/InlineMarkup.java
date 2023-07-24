package telegram.billbot.inlinemarkups;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;


public class InlineMarkup {

    public InlineKeyboardMarkup createSplitButton(Long chatId, String encryptedUser) {
        InlineKeyboardMarkup ikm = new InlineKeyboardMarkup();
        InlineKeyboardButton ikb = new InlineKeyboardButton("Split bill");
        ikb.setCallbackData("split bill");
        String url = "https://billbuddy.jeraldlim.com/#/start?qs="
                    + encryptedUser;
        System.out.println(url);
        ikb.setUrl(url);
        
        List<InlineKeyboardButton> buttonRow = new ArrayList<>();
        buttonRow.add(ikb);
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(buttonRow);
        ikm.setKeyboard(keyboard);
        
        return ikm;
    }
}

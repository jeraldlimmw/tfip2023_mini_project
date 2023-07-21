package telegram.billbot;

import static telegram.billbot.constants.Information.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import telegram.billbot.inlinemarkups.InlineMarkup;
import telegram.billbot.services.Encryptor;

@Component
public class Billbot extends TelegramLongPollingBot{
    
    private static Environment env;

    @Autowired
    public void setEnv(Environment env) {
        Billbot.env = env;
    }

    @Override
    public String getBotUsername() {
        return env.getProperty("telegram.bot.username");
    }

    @Override
    public String getBotToken() {
        return env.getProperty("telegram.bot.token");
    }

    public String getEncryptionKey() {
        return env.getProperty("telegram.bot.encryption.key");
    }
	
	@Override
    public void onUpdateReceived(Update update) {
		Encryptor encryptor = new Encryptor();

        System.out.println(">>>> Update received");
        if (update.hasMessage() && update.getMessage().hasText()) {
			String text = update.getMessage().getText();
			System.out.println(text);
			long chatId = update.getMessage().getChatId();
            System.out.println(chatId);
			String br = ":::";
			String userData = String.valueOf(chatId)
					+ br + update.getMessage().getFrom().getId().toString() 
					+ br + update.getMessage().getFrom().getFirstName() 
					+ br + update.getMessage().getFrom().getUserName();
			String encryptedData = encryptor.encrypt(userData, getEncryptionKey());
			System.out.println(encryptedData);

			SendMessage message = new SendMessage();

			switch (text) {
				case "/start" -> message = onStartCmd(chatId, encryptedData);

				case "/start@billbuddy_bot" -> message = onStartCmd(chatId, encryptedData);
				
				case "/help" -> message = onHelpCmd(chatId);

				case "/help@billbuddy_bot" -> message = onHelpCmd(chatId);

				default -> message = onUnknownCmd(chatId);
			}

			try{
				execute(message);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		} 
    }

	private SendMessage onStartCmd(Long chatId, String encryptedData) {
        SendMessage message = new SendMessage();
		message.setChatId(chatId);
		message.setText(START_MESSAGE);
        InlineMarkup im = new InlineMarkup();
		message.setReplyMarkup(im.createSplitButton(chatId, encryptedData));
		return message;
    }

	private SendMessage onHelpCmd(Long chatId) {
        SendMessage message = new SendMessage();
		message.setChatId(chatId);
        message.setText(HELP_INFO);
		return message;
    }

    private SendMessage onUnknownCmd(Long chatId) {
		SendMessage message = new SendMessage();
		message.setChatId(chatId);
        message.setText(
			"Sorry, I couldn't understand that. Try using one of the commands or the inline buttons."
		);
		return message;    
	}
}

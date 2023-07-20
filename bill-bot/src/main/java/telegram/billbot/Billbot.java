package telegram.billbot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import jakarta.annotation.PreDestroy;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import telegram.billbot.inlinemarkups.InlineMarkup;
import telegram.billbot.models.User;

import static telegram.billbot.constants.Information.*;

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
	
	@Override
    public void onUpdateReceived(Update update) {
        System.out.println(">>>> Update received");
        if (update.hasMessage() && update.getMessage().hasText()) {
			String text = update.getMessage().getText();
			System.out.println(text);
			long chatId = update.getMessage().getChatId();
            System.out.println(chatId);
			User user = new User();
			user.setFirstName(update.getMessage().getFrom().getFirstName());
			user.setUsername(update.getMessage().getFrom().getUserName());
			user.setUserId(update.getMessage().getFrom().getId());

			SendMessage message = new SendMessage();

			switch (text) {
				case "/start" -> message = onStartCmd(chatId, user);

				case "/start@billbuddy_bot" -> message = onStartCmd(chatId, user);
				
				case "/help" -> message = onHelpCmd(chatId);

				default -> message = onUnknownCmd(chatId);
			}

			try{
				execute(message);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		} 
		/* 
		else if (update.hasCallbackQuery()) {
			CallbackQuery callback = update.getCallbackQuery();
			String data = callback.getData();
			Long chatId = callback.getMessage().getChatId();

			switch (data) {
				case "Split bill":
					// String firstName = callback.getFrom().getFirstName();
					// String username = callback.getFrom().getUserName();
					// Long userId = callback.getFrom().getId();

					// redirectToWebpage(chatId, firstname, username, userId);
					break;
				// case "paid":
					
                //     pmToCreator(userId);
                //     break;
				default:
					;
			}
			
			AnswerCallbackQuery answer = new AnswerCallbackQuery();
			answer.setCallbackQueryId(callback.getId());

			try {
				execute(answer);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}
		*/
    }

	private SendMessage onStartCmd(Long chatId, User user) {
        SendMessage message = new SendMessage();
		message.setChatId(chatId);
		message.setText(START_MESSAGE);
        InlineMarkup im = new InlineMarkup();
		message.setReplyMarkup(im.createSplitButton(chatId, user));
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
			"Sorry, I couldn't understand that. Try using the inline buttons."
		);
		return message;    
	}

	/*
    private void payCmd() {

    }

    private void paidCmd() {

    } 

	private void redirectToWebpage(Long chatId, String firstname, String username, Long userId) {
		JsonObject jo = Json.createObjectBuilder()
							.add("chatId", chatId)
							.add("firstname", firstname)
							.add("username", username)
							.add("userId", userId)
							.build();
		
		RestTemplate template = new RestTemplate();

		RequestEntity<String> request = RequestEntity.post(null)
									.accept(MediaType.APPLICATION_JSON)
									.body(jo.toString());

		ResponseEntity<String> response = template.exchange(request, String.class);

		System.out.println(response.getBody());
	}
	*/

	@PreDestroy
	public void destroy() {
		
	}
}

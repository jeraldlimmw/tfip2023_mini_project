package telegram.billbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import jakarta.annotation.PreDestroy;

@SpringBootApplication
public class BillbotApplication{
	private static BotSession session;

	public static void main(String[] args) throws TelegramApiException {
		System.out.println(">>>> Starting app");
		SpringApplication.run(BillbotApplication.class, args);
		
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

		try {
			session = telegramBotsApi.registerBot(new Billbot());
		} catch (Exception e){
			e.printStackTrace();
			stopBotSession();
		}
	}

	@PreDestroy
    public static void stopBotSession() {
        if (session != null) {
            session.stop();
        }
    }
}

package telegram.billbot.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import telegram.billbot.models.User;
import telegram.billbot.models.SettlementMessage;
import telegram.billbot.inlinemarkups.InlineMarkup;

@RestController
public class MessageController {
    
    @PostMapping(path="/message")
    public void receiveSettlement(@RequestBody String json) throws IOException {
        // take message and process message
        SettlementMessage sm = new SettlementMessage();
        User creator = new User();
        try (InputStream is = new ByteArrayInputStream(json.getBytes())) {
            JsonReader r = Json.createReader(is);
            JsonObject o = r.readObject();

            sm.setChatId(Long.parseLong(o.getString("chatId")));
            sm.setText(o.getString("text"));
            JsonObject creatorJson = o.getJsonObject("creator");
            creator.setUserId(Long.parseLong(creatorJson.getString("userId")));
            creator.setUsername(creatorJson.getString("username"));
            creator.setFirstName(creatorJson.getString("firstname"));
            sm.setBillCreator(creator);
        }
        
        InlineMarkup im = new InlineMarkup();

        if (!Objects.isNull(sm)) {
            SendMessage settlement = new SendMessage();
            settlement.setChatId(sm.getChatId());
            settlement.setText(sm.getText());
            settlement.setReplyMarkup(im.paymentMenu());
        }
    }
}

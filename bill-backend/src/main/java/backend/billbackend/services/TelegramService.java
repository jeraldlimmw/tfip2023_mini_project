package backend.billbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import backend.billbackend.models.Bill;
import backend.billbackend.repositories.BillRepository;

@Service
public class TelegramService {
    @Autowired
    private BillRepository bRepo;

    @Autowired
    private SettlementMessageService msgSvc;

    @Value("${telegram.bot.url}")
    private String telegramBotUrl;

    public void sendSettlement(String billId) {
        System.out.println(">>>> In Tele Service: Bill ID = " + billId);
        Bill bill = bRepo.findBillById(billId);
        String url = telegramBotUrl + "/sendMessage";
        System.out.println(">>>> In Tele Service: url = " + url);
        String message = msgSvc.constructSettlementMessage(bill);
        System.out.println(">>>> In Tele Service: message = " + message);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("chat_id", bill.getChatId());
        form.add("text", message);

        RestTemplate template = new RestTemplate();
        RequestEntity<MultiValueMap<String, String>> request = RequestEntity
                .post(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form);
        ResponseEntity<String> response = template.exchange(request, String.class);      
    }
}

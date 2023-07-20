package backend.billbackend.services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
import backend.billbackend.models.Transaction;
import backend.billbackend.repositories.BillRepository;
import backend.billbackend.repositories.TransactionRepository;

@Service
public class TelegramService {
    @Autowired
    private BillRepository bRepo;

    @Autowired
    private TransactionRepository tRepo;

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

    // private String constructSettlementMessage(Bill bill) {
    //     List<Transaction> transactions = tRepo.findTransactionsByBillId(bill.getBillId());
    //     DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy, HH:mm:ss");

    //     String header = String.format("Bill Title: %s\nCreated by %s on %s\n\n",
    //             bill.getTitle(), bill.getUser().getFirstName(), 
    //             df.format(new Date(bill.getTimestamp())));
    //     String body = "How to settle up:\n";
    //     for(Transaction t : transactions) {
    //         body += String.format("- %s pay %s $%.2f\n", 
    //         t.getPayer(), t.getPayee(), t.getAmount());
    //     }
    //     body += "\n";
    //     String signoff = "Kindly brought to you by billbuddy!";
        
    //     return header + body + signoff;
    // }
}

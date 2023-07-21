package backend.billbackend.controllers;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import backend.billbackend.models.Bill;
import backend.billbackend.models.Settlement;
import backend.billbackend.models.StartData;
import backend.billbackend.models.User;
import backend.billbackend.services.BillEmailService;
import backend.billbackend.services.DecryptionService;
import backend.billbackend.services.SplitService;
import backend.billbackend.services.TelegramService;
import jakarta.mail.MessagingException;

@Controller
@RequestMapping(path="/bill")
public class BillController {
    @Autowired
    private SplitService splitSvc;

    @Autowired
    private BillEmailService emailSvc;

    @Autowired
    private TelegramService teleSvc;

    @Autowired
    private DecryptionService decryptSvc;

    // Post method to get Bill object
    @PostMapping(path="/store")
    @ResponseBody
    public ResponseEntity<String> postBill(@RequestBody String json) throws JsonMappingException, JsonProcessingException {
        // System.out.println(">>>> Controller: Json " + json);
        ObjectMapper mapper = new ObjectMapper();
        Bill bill = mapper.readValue(json, Bill.class);
        System.out.println(">>>> Controller: Bill received " + bill);

        String billId = splitSvc.split(bill);

        if (Objects.isNull(billId)) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{ error : bill not stored }");
        } else {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{ \"billId\" : \"" + billId + "\" }");
        }
    }

    // Get Settlement with array of transactions based on bill
    @GetMapping(path="/settlement")
    @ResponseBody
    public ResponseEntity<String> getSettlementFromBillId(@RequestParam String id) {
        Settlement settlement = splitSvc.getSettlement(id);
        
        if (Objects.isNull(settlement)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{ error : billId not found }");
        }

        ObjectMapper mapper = new ObjectMapper();
        String settlementJson = "";
        try {
            settlementJson = mapper.writeValueAsString(settlement);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok()
                    .body(settlementJson);
    }

    @PostMapping(path="/email/{id}")
    @ResponseBody
    public ResponseEntity<String> sendEmailByBillId(@PathVariable String id, @RequestBody String recipient) {
        System.out.println(">>>> Controller: bill id = " +  id);
        System.out.println(">>>> Controller: recipient = " + recipient);
        
        try {
            emailSvc.sendBill(id, recipient);
        } catch (MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok("{\"message\": \"email sent\"}");
    }

    @PostMapping(path="/telegram")
    @ResponseBody
    public ResponseEntity<String> sendSettlementToTelegram(@RequestBody String id) {
        try {
            teleSvc.sendSettlement(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok("{\"message\": \"telegram sent\"}");
    }

    @GetMapping(path="/start")
    @ResponseBody
    public ResponseEntity<String> startBillFromTelegramUrl(@RequestParam String qs) {
        String userData = decryptSvc.decrypt(qs);
        String[] data = userData.split(":{3}");

        StartData startData = new StartData();
        startData.setChatId(Long.parseLong(data[0]));
        User user = new User();
        user.setUserId(Long.parseLong(data[1]));
        user.setFirstName(data[2]);
        user.setUsername(data[3]);
        startData.setUser(user);
        System.out.println(">>>> Data: " + startData.toString());

        ObjectMapper mapper = new ObjectMapper();
        String startDataJson = "";
        try {
            startDataJson = mapper.writeValueAsString(startData);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok()
                .body(startDataJson);
    }
}

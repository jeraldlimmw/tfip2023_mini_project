package backend.billbackend.controllers;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import backend.billbackend.models.Bill;
import backend.billbackend.services.SplitService;

@Controller
@RequestMapping(path="/bill")
public class BillController {
    @Autowired
    private SplitService splitSvc;

    // Post method to get Bill object
    @PostMapping()
    @ResponseBody
    public ResponseEntity<String> postBill(@RequestBody String json) throws JsonMappingException, JsonProcessingException {
        System.out.println(">>>> Controller: Json " + json);
        ObjectMapper objectMapper = new ObjectMapper();
        Bill bill = objectMapper.readValue(json, Bill.class);
        System.out.println(">>>> Controller: Bill received " + bill);

        String billId = splitSvc.split(bill);

        return ResponseEntity.status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{ billId : \"" + billId + "\" }");
    }

    // Get Settlement with array of transactions based on bill
    @GetMapping(path="/{id}")
    @ResponseBody
    public ResponseEntity<String> getSettlementFromBillId(@PathVariable String id) {
        splitSvc.getSettlement(id);

        return ResponseEntity.ok()
                    .body(null);
    }
}

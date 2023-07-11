package backend.billbackend.controllers;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import backend.billbackend.models.Receipt;
import backend.billbackend.services.ReceiptService;

@RestController
@RequestMapping(path="/receipt")
public class ReceiptController {
    
    @Autowired
    private ReceiptService receiptSvc;

    @PostMapping(path="/upload")
    public ResponseEntity<String> postReceiptPhoto(@RequestBody String dataUrl) throws IOException {
        
        Optional<Receipt> receipt = receiptSvc.receiptResponseFromOcr(dataUrl);
        ObjectMapper mapper = new ObjectMapper();
        String receiptJsonString = "";
        if (receipt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("error: unable to read receipt");
        }

        try {
            receiptJsonString = mapper.writeValueAsString(receipt.get());
        } catch(JsonProcessingException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(receiptJsonString);
    }
}

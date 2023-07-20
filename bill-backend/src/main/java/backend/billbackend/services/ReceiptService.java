package backend.billbackend.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import backend.billbackend.models.Receipt;

@Service
public class ReceiptService {
   
    private String receiptOcrEndpoint = "https://ocr.asprise.com/api/v1/receipt";

    public Optional<Receipt> receiptResponseFromOcr(String dataUrl) throws IOException {
        
        RestTemplate template = new RestTemplate();

        System.out.println(">>>> In Receipt Service: ");
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("api_key", "TEST");
        body.add("recognizer", "SG");
        //body.add("ref_no", "");
        File imageFile = base64ToFile(dataUrl, "receipt.jpeg");
        body.add("file", new FileSystemResource(imageFile));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = template.exchange(receiptOcrEndpoint, 
                                HttpMethod.POST, entity, String.class);

        Receipt finalReceipt = Receipt.create(response.getBody());
        System.out.println(">>>> In Receipt Service: " + finalReceipt);
        if (finalReceipt != null) {
            return Optional.of(finalReceipt);
        } else {
            return Optional.empty();
        }
    }

    private File base64ToFile(String dataurl, String filename) throws FileNotFoundException, IOException {
        String[] arr = dataurl.split(",");
        // String mime = arr[0].split(":")[1].split(";")[0];
        String base64Data = arr[arr.length - 1];

        byte[] bytes = Base64.getDecoder().decode(base64Data);
        File file = new File(filename);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(bytes);
        }

        return file;
    }
}

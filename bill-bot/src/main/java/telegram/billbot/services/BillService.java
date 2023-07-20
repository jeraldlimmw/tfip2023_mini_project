// package telegram.billbot.services;

// import org.springframework.http.MediaType;
// import org.springframework.http.RequestEntity;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.client.RestTemplate;

// import jakarta.json.Json;
// import jakarta.json.JsonObject;

// import telegram.billbot.inlinemarkups.InlineMarkup;

// public class BillService {
//     private void sendToWebPage(Long chatId, String firstname, String username) {
//         JsonObject jo = Json.createObjectBuilder()
//                             .add("chatId", chatId)
//                             .add("firstname", firstname)
//                             .add("username", username)
//                             .build();
        
//         RestTemplate template = new RestTemplate();

// 		RequestEntity request = RequestEntity.post(null)
//                                     .accept(MediaType.APPLICATION_JSON)
//                                     .body(jo.toString());

// 		ResponseEntity<String> payload = template.exchange(request, String.class);
//     }
// }

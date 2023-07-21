package backend.billbackend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import backend.billbackend.models.Bill;
import backend.billbackend.models.Item;
import backend.billbackend.models.Transaction;
import backend.billbackend.repositories.BillRepository;
import backend.billbackend.repositories.TransactionRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class BillEmailService {

    @Autowired
    private JavaMailSender sender;

    @Autowired
    private BillRepository bRepo;

    @Autowired
    private TransactionRepository tRepo;
    
    public void sendBill(String billId, String recipient) throws MessagingException {
        Bill bill = bRepo.findBillById(billId);
        System.out.println(">>>> Service: In email service, bill = \n"
                + bill + "\n");
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(recipient);
        helper.setFrom("billbuddy@jeraldlim.com");
        helper.setSubject("Your bill split - " + bill.getTitle());

        // set email contents
        helper.setText(constructHtmlText(bill), true);

        sender.send(message);
    }

    private String constructHtmlText(Bill bill) {
        String htmlText = "<html><body>"
        + "<div><p>" + constructBillStarter(bill) + "</p></div>"
        + constructBillHtmlTable(bill)
        + "<div><p>" + constructBillSummary(bill) + "</p></div>"
        + "</body></html>";
        return htmlText;
    }

    private String constructBillStarter(Bill bill) {
        List<Transaction> transactions = 
                tRepo.findTransactionsByBillId(bill.getBillId());

        String intro = "Your bill has been split!<br><br>"
                + String.format("Bill Title: %s<br><br>", bill.getTitle())
                + "How to settle up:<br>";

        for (Transaction t : transactions) {
            intro = intro + String.format("  - %s pay %s $%.2f", 
                    t.getPayer(), t.getPayee(), t.getAmount())
                    + "<br>";
        }
        intro += "<br><hr><br>";
        return intro;
    }

    private String constructBillHtmlTable(Bill bill) {
        String share = (bill.getByPercentage()) ? "Percentage" : "Share";

        String tableHtml = "<div><table>Your bill:<br><br>";
        String headers = "<tr>"
                + "<th rowspan=\"2\" style=\"border: 1px solid black; padding: 5px;\">No</th>"
                + "<th rowspan=\"2\" style=\"border: 1px solid black; padding: 5px;\">Item</th>"
                + "<th rowspan=\"2\" style=\"border: 1px solid black; padding: 5px;\">Price</th>"
                + "<th rowspan=\"2\" style=\"border: 1px solid black; padding: 5px;\">Quantity</th>"
                + "<th colspan=\"" + bill.getFriends().size() +  "\" style=\"border: 1px solid black; padding: 5px;\">" + String.format("%s</th>", share)
                + "</tr>"
                + "<tr>"; 
        for (String f : bill.getFriends()) {
            headers += String.format("<th style=\"border: 1px solid black; padding: 5px;\">%s</th>", f);
        }
        headers += "</tr>";

        String body = "";
        for (Item i : bill.getItems()) {
            body = body 
                    + "<tr>"
                    + String.format("<td style=\"border: 1px solid black; padding: 5px;\">%s</td>", bill.getItems().indexOf(i) + 1)
                    + String.format("<td style=\"border: 1px solid black; padding: 5px;\">%s</td>", i.getItemName())
                    + String.format("<td style=\"border: 1px solid black; padding: 5px;\">%.2f</td>", i.getPrice())
                    + String.format("<td style=\"border: 1px solid black; padding: 5px;\">%d</td>", i.getQuantity());
            if (bill.getByPercentage()) {
                for (Double p : i.getPercentShares()) {
                    body = body 
                            + String.format("<td style=\"border: 1px solid black; padding: 5px;\">%.2f", p) + "%</td>";
                }
            } else {
                for (Double s : i.getShares()) {
                    body = body 
                            + String.format("<td style=\"border: 1px solid black; padding: 5px;\">%.2f</td>", s);
                }
            }
            body += "</tr>";
        }
        tableHtml = tableHtml + headers + body + "</table></div>";

        return tableHtml;
    }

    private String constructBillSummary(Bill bill) {        
        String summary = "";
        if (bill.getService() > 0) {
            summary = summary 
                    + String.format("Service: %.2f", bill.getService())
                    + "%<br>";
        }
        if (bill.getTax() > 0) {
            summary = summary 
                    + String.format("Tax (GST): %.2f", bill.getTax())
                    + "%<br>";
        }
        summary = summary 
                + String.format("Total Expenditure: $ %.2f", bill.getTotal())
                + "<br><br>"
                + "Kindly brought to you by blllbuddy!";
                // + "<br><br>"
                // + "#IMAGE PLACEHOLDER";

        return summary;
    }
}

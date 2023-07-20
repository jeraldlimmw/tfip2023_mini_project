package backend.billbackend.services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend.billbackend.models.Bill;
import backend.billbackend.models.Transaction;
import backend.billbackend.repositories.TransactionRepository;

@Service
public class SettlementMessageService {
    
    @Autowired
    private TransactionRepository tRepo;
    
    public String constructSettlementMessage(Bill bill) {
        List<Transaction> transactions = tRepo.findTransactionsByBillId(bill.getBillId());
        DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy, HH:mm:ss");

        String header = String.format("Bill Title: %s\nCreated by %s on %s\n\n",
                bill.getTitle(), bill.getUser().getFirstName(), 
                df.format(new Date(bill.getTimestamp())));
        String body = "How to settle up:\n";
        for(Transaction t : transactions) {
            body += String.format("- %s pay %s $%.2f\n", 
            t.getPayer(), t.getPayee(), t.getAmount());
        }
        body += "\n";
        String signoff = "Kindly brought to you by billbuddy!";
        
        return header + body + signoff;
    }
}

package backend.billbackend.services;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import backend.billbackend.models.Bill;
import backend.billbackend.models.Item;
import backend.billbackend.models.Paid;
import backend.billbackend.models.Settlement;
import backend.billbackend.models.Transaction;
import backend.billbackend.models.User;
import backend.billbackend.repositories.BillRepository;
import backend.billbackend.repositories.TransactionRepository;

@Service
public class SplitService {
    
    @Autowired
    private BillRepository billRepo;

    @Autowired
    private TransactionRepository transRepo;

    private List<User> payees = new LinkedList<>();
    private List<User> payers = new LinkedList<>();
    private List<User> users = new LinkedList<>();
    private List<Transaction> transactions = new LinkedList<>();

    public String split(Bill bill) {
        // TODO: add transactional, and put SQL in front of Mongo so that it can rollback
        // Store Bill in Mongo
        bill.setBillID(generateUUID());

        List<String> payeeNames = new ArrayList<>();

        // Add people to Users list - people who paid will have -ve balance
        for (Paid p : bill.getPaid()) {
            users.add(new User(p.getName(), (0.0 - p.getAmount())));
            payeeNames.add(p.getName());
        }
        System.out.println(">>>> Service: Users (Ppl who paid)" + users);

        for (String f : bill.getFriends()) {
            if (!payeeNames.contains(f)) {
                users.add(new User(f, 0.0));
            }
        }
        System.out.println(">>>> Service: Users (Add in everyone else)" + users);

        // Add expenses to each User
        addExpenses(bill);
        // Calculate balance for Payees and sort in descending order of balance
        sortPayees();
        // Sort Payers in descending order of expense
        sortPayers();
        // Calculate transactions
        generateTransactions(bill.getBillId());
        // Store transactions in SQL and bill in Mongo
        return store(bill, transactions);
    }

    public Settlement getSettlement(String id) {
        Bill b = billRepo.findBillById(id);
        Settlement s = new Settlement(id);
        s.setTitle(b.getTitle());
        s.setTimestamp(b.getTimestamp());
        s.setTransactions(transRepo.findTransactionsByBillId(id));
        System.out.println(">>>> Service: Settlement " + s);
        return s;
    }

    private void addExpenses(Bill bill) {
        for (Item i : bill.getItems()) {
            System.out.println(">>>> Service: Item i = " + i);
            double finalPrice = 
                    (i.getPrice() * (bill.getService() + 1)) * (bill.getTax() + 1);
            double sharePerPax = finalPrice / i.getPeople().size();
            System.out.println(">>>> Service: per share of " + i.getItemName() + " = " + sharePerPax);
            for (User u : users) {
                if (i.getPeople().contains(u.getName())) {
                    u.setExpense(u.getExpense() + sharePerPax);
                }
            }
        }
        // Calculate balance for all users after expenses calculated
        for (User u : users) {
            u.setBalance(u.getBalance() + u.getExpense());
        }
        System.out.println(">>>> Service: Users " + users);
    }

    private void sortPayees() {
        payees = users.stream()
                .filter(u -> u.getBalance() < 0)
                .sorted((u1, u2) -> u1.getBalance().compareTo(u2.getBalance()))
                .collect(Collectors.toList());
        System.out.println(">>>> Service: Sorted Payees " + payees);
    }

    private void sortPayers() {
        payers = users.stream()
                .filter(u -> u.getBalance() >= 0)
                .sorted((u1, u2) -> u2.getExpense().compareTo(u1.getExpense()))
                .collect(Collectors.toList());
        System.out.println(">>>> Service: Sorted Payers " + payers);
    }

    private void generateTransactions(String billId) {
        // while there are payees,
        /* case 1: if there is only one payee,
         * for each payer, pay their remaining balance to the payee
         * new Transaction(tId, billId, payer n, payee, payer n's balance)
         */
        /* case 2: if there are more than one payee
         * if payer 0 balance + payee 0 balance < 0, 
         * payer 0 pays balance to payee 0, set new balance for payee
         * new Transaction(tId, billId, payer n, payee 0, payer n's balance)
         * remove payer n
         * 
         * else, (payer 0 balance has reached 0)
         * payer pays payee's balance * -1
         * new Transaction(tId, billId, payer n, payee 0, payee 0 balance * -1)
         * set new payer balance
         * remove payee
         */
        while (!payees.isEmpty()) {
            if (payees.size() == 1) {
                for (User p : payers) {
                    transactions.add(new Transaction("t" + generateUUID(), billId, 
                            p.getName(), payees.get(0).getName(),
                            p.getBalance()));
                }
                payees.remove(0);
            } else {
                if (payees.get(0).getBalance() 
                        + payers.get(0).getBalance() < 0) {                    
                    transactions.add(new Transaction("t" + generateUUID(), billId, 
                            payers.get(0).getName(), payees.get(0).getName(),
                            payers.get(0).getBalance()));
                    User toUpdatePayee = payees.get(0);
                    toUpdatePayee.setBalance(
                            payees.get(0).getBalance() 
                            + payers.get(0).getBalance());
                    payees.set(0, toUpdatePayee);
                    payers.remove(0);
                } else {
                    transactions.add(new Transaction("t" + generateUUID(), billId, 
                            payers.get(0).getName(), payees.get(0).getName(),
                            -1.0 * payees.get(0).getBalance()));
                    User toUpdatePayer = payers.get(0);
                    toUpdatePayer.setBalance(payees.get(0).getBalance() 
                            + payers.get(0).getBalance());
                    payees.remove(0);
                }
            }
        }
        System.out.println(">>>> Service: transactions " + transactions);   
    }

    @Transactional
    private String store(Bill bill, List<Transaction> transactions) {
        int rowsInserted = 0;
        for (Transaction t : transactions) {
            transRepo.insertTransaction(t);
            rowsInserted++;
        }
        System.out.println(">>>> Service: Transactions added = " + rowsInserted);
        System.out.println(">>>> Service: Transactions " + transactions);
        billRepo.insertBill(bill);
        if (rowsInserted == transactions.size()) {
            return bill.getBillId();
        }
        return null;
    }

    // private String saveBill(Bill bill) {
    //     bill.setBillID(generateUUID());
    //     billRepo.insertBill(bill);
    //     return bill.getBillId();
    // }

    // private int saveTransactions(List<Transaction> transactions) {
    //     int rowsInserted = 0;
    //     for (Transaction t : transactions) {
    //         transRepo.insertTransaction(t);
    //         rowsInserted++;
    //     }
    //     System.out.println(">>>> Service: Transactions added = " + rowsInserted);
    //     System.out.println(">>>> Service: Transactions " + transactions);
    //     return rowsInserted;
    // }

    // generate 8 char UUID
    private String generateUUID() { 
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
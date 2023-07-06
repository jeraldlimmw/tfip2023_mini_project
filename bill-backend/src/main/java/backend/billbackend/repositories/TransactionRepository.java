package backend.billbackend.repositories;

import java.sql.PreparedStatement;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import backend.billbackend.models.Transaction;
import static backend.billbackend.repositories.DBQueries.*;

@Repository
public class TransactionRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int insertTransaction(Transaction t) {
        return jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(INSERT_TRANSACTION);
            ps.setString(1, t.getId());
            ps.setString(2, t.getBillId());
            ps.setString(3, t.getPayer());
            ps.setString(4, t.getPayee());
            ps.setDouble(5, t.getAmount());
            return ps;
        });
    }

    public List<Transaction> findTransactionsByBillId(String id) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SELECT_TRANSACTIONS_BY_BILL_ID, id);

        List<Transaction> tList = new LinkedList<>();
        Transaction t = new Transaction();
        
        while (rs.next()) {
            t.setId(rs.getString("id"));
            t.setBillId(rs.getString("billId"));
            t.setPayee(rs.getString("payee"));
            t.setPayer(rs.getString("payer"));
            t.setAmount(rs.getDouble("amount"));
            tList.add(t);
        }
        return tList;
    }

}

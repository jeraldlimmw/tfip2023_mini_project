package backend.billbackend.repositories;

public class DBQueries {
    public static final String INSERT_TRANSACTION = """
            insert into transactions (id, billId, payer, payee, amount)
            values (?,?,?,?,?)
            """;
    
    public static final String SELECT_TRANSACTIONS_BY_BILL_ID = """
            select id, billId, payer, payee, amount
            from transactions
            where billId like ?
            """;
}

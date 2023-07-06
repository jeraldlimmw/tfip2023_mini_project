package backend.billbackend.repositories;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import backend.billbackend.models.Bill;

@Repository
public class BillRepository {
    @Autowired
    private MongoTemplate mongoTemplate;

    public void insertBill(Bill b) {
        mongoTemplate.insert(b, "bills");
    }

    public Bill findBillById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("billId").is(id));

        mongoTemplate.find(query, Document.class, "bills");
        return null;
    }

}

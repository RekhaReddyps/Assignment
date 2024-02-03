package com.maveric.transactionservice.repository;

import com.maveric.transactionservice.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {

    Page<Transaction> findTransactionByAccountId(Pageable pageable, String accountId);

    List<Transaction> deleteAllTransactionsByAccountId(String accountId);
}

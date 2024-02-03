package com.maveric.transactionservice.converter;

import com.maveric.transactionservice.dto.TransactionDto;
import com.maveric.transactionservice.model.Transaction;
import org.springframework.stereotype.Component;

@Component
public class DtoToModelConverter {
    public Transaction dtoToModel(TransactionDto transactionDto) {
        Transaction transaction = new Transaction();

        transaction.set_id(transactionDto.get_id());
        transaction.setAccountId(transactionDto.getAccountId());
        transaction.setAmount(transactionDto.getAmount());
        transaction.setType(transactionDto.getType());
        transaction.setCreatedAt(transactionDto.getCreatedAt());

        return transaction;
    }

    public TransactionDto modelToDto(Transaction transaction){
        TransactionDto transactionDto = new TransactionDto();

        transactionDto.set_id(transaction.get_id());
        transactionDto.setAmount(transaction.getAmount());
        transactionDto.setType(transaction.getType());
        transactionDto.setAccountId(transaction.getAccountId());
        transactionDto.setCreatedAt(transaction.getCreatedAt());

        return transactionDto;
    }
}

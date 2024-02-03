package com.maveric.transactionservice.converter;

import com.maveric.transactionservice.constant.Type;
import com.maveric.transactionservice.dto.TransactionDto;
import com.maveric.transactionservice.model.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DtoToModelConverterTest {

    @InjectMocks
    DtoToModelConverter dtoToModelConverter;

    @Test
    void dtoToModel() {
        TransactionDto transactionDto = getTransactionDto();
        Transaction transaction = dtoToModelConverter.dtoToModel(getTransactionDto());
        assertNotNull(transaction.getAccountId());
        assertSame(transaction.getAccountId(), transactionDto.getAccountId());
    }

    @Test
    void modelToDto() {
        Transaction transaction = getTransaction();
        TransactionDto transactionDto = dtoToModelConverter.modelToDto(getTransaction());
        assertNotNull(transactionDto.getAccountId());
        assertSame(transaction.getAccountId(), transactionDto.getAccountId());
    }

    public static Transaction getTransaction() {
        Transaction transaction = new Transaction();
        transaction.setAmount(1000);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setAccountId("6efdh");
        transaction.setType(Type.CREDIT);

        return transaction;
    }

    public static TransactionDto getTransactionDto() {
        TransactionDto transaction = new TransactionDto();
        transaction.setAmount(1000);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setAccountId("6efdh");
        transaction.setType(Type.CREDIT);

        return transaction;
    }
}
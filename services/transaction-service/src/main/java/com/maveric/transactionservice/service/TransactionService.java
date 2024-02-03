package com.maveric.transactionservice.service;

import com.maveric.transactionservice.dto.TransactionDto;
import com.maveric.transactionservice.exception.AccountIdMismatchException;
import com.maveric.transactionservice.exception.TransactionIdNotFoundException;
import java.util.List;
public interface TransactionService {

    TransactionDto createTransaction (TransactionDto transactionDto, String accountId);

    List<TransactionDto> getTransactionByAccountId(int page, int pageSize, String accountId);

    TransactionDto getTransactionIdByAccountId(String accountId, String transactionId) throws TransactionIdNotFoundException, AccountIdMismatchException;

    void deleteTransactionIdByAccountId(String accountId, String transactionId) throws TransactionIdNotFoundException, AccountIdMismatchException;

    void deleteAllTransactionsByAccountId(String accountId);
}

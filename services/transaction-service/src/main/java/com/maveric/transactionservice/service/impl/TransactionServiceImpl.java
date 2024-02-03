package com.maveric.transactionservice.service.impl;

import com.maveric.transactionservice.converter.DtoToModelConverter;
import com.maveric.transactionservice.dto.TransactionDto;
import com.maveric.transactionservice.exception.AccountIdMismatchException;
import com.maveric.transactionservice.exception.TransactionIdNotFoundException;
import com.maveric.transactionservice.model.Transaction;
import com.maveric.transactionservice.repository.TransactionRepository;
import com.maveric.transactionservice.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    DtoToModelConverter dtoToModelConverter;

    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Override
    public TransactionDto createTransaction(TransactionDto transactionDto, String accountId) {
        if (accountId.equals(transactionDto.getAccountId())) {
            Transaction transaction = dtoToModelConverter.dtoToModel(transactionDto);
            logger.info("New transaction created for account id");
            return dtoToModelConverter.modelToDto(transactionRepository.save(transaction));
        } else {
            logger.info("Account Id not found");
            throw new AccountIdMismatchException("The account ID " + accountId + " is not available");
        }
    }

    @Override
    public List<TransactionDto> getTransactionByAccountId(int page, int pageSize, String accountId) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Transaction> transactionPage = transactionRepository.findTransactionByAccountId(pageable, accountId);

        List<Transaction> transactionList = transactionPage.getContent();
        logger.info("Getting list of transaction for account id");
        return transactionList.stream().map(transaction -> dtoToModelConverter.modelToDto(transaction)).toList();
    }

    @Override
    public TransactionDto getTransactionIdByAccountId(String accountId, String transactionId) throws TransactionIdNotFoundException, AccountIdMismatchException {
        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(
                () -> new TransactionIdNotFoundException("Transaction id not available")
        );
        if(accountId.equals(transaction.getAccountId())) {
            logger.info("Returning transaction for transaction id");
            return dtoToModelConverter.modelToDto(transaction);
        } else {
            logger.info("Account Id not available");
            throw new AccountIdMismatchException("Account Id " + accountId + " not available");
        }
    }

    @Override
    public void deleteTransactionIdByAccountId(String accountId, String transactionId) throws TransactionIdNotFoundException, AccountIdMismatchException {
        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(
                () -> new TransactionIdNotFoundException("Transaction ID not available")
        );
        if(accountId.equals(transaction.getAccountId())) {
            logger.info("Transaction deleted");
            transactionRepository.deleteById(transactionId);
        } else {
            throw new AccountIdMismatchException("Account Id " + accountId + " not available");
        }
    }

    @Override
    public void deleteAllTransactionsByAccountId(String accountId) {
        logger.info("All transactions deleted");
        transactionRepository.deleteAllTransactionsByAccountId(accountId);
    }
}

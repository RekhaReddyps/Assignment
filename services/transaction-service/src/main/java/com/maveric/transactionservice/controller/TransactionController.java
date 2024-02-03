package com.maveric.transactionservice.controller;

import com.maveric.transactionservice.constant.MessageConstant;
import com.maveric.transactionservice.constant.Type;
import com.maveric.transactionservice.dto.AccountDto;
import com.maveric.transactionservice.dto.BalanceDto;
import com.maveric.transactionservice.dto.TransactionDto;
import com.maveric.transactionservice.exception.AccountIdMismatchException;
import com.maveric.transactionservice.exception.TransactionAmountException;
import com.maveric.transactionservice.exception.TransactionIdNotFoundException;
import com.maveric.transactionservice.feignclient.FeignAccountConsumer;
import com.maveric.transactionservice.feignclient.FeignBalanaceConsumer;
import com.maveric.transactionservice.service.TransactionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class TransactionController {
    @Autowired
    TransactionService transactionService;

    @Autowired
    FeignBalanaceConsumer feignBalanaceConsumer;

    @Autowired
    FeignAccountConsumer feignAccountConsumer;

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);


    @PostMapping("accounts/{accountId}/transactions")
    public ResponseEntity<TransactionDto> createUser(@PathVariable("accountId") String accountId,
                                                     @Valid @RequestBody TransactionDto transactionDto,
                                                     @RequestHeader(value = "userid") String headerUserId) {
        AccountDto accountDto = feignAccountConsumer.getAccount(headerUserId, accountId, headerUserId);
        if(accountDto.get_id().equals(transactionDto.getAccountId())){
            ResponseEntity<BalanceDto> balanceDto = feignBalanaceConsumer.getAllBalanceByAccountId(accountId, headerUserId);
            logger.info("Returning list of balance for account id"); //NOSONAR
            BalanceDto currentUserBalance = balanceDto.getBody(); //NOSONAR
            Number newBalance;
            Number transactionAmount = transactionDto.getAmount();
            Number balanceAmount = currentUserBalance.getAmount(); //NOSONAR

            if(transactionDto.getType().equals(Type.DEBIT) && (transactionAmount.doubleValue() < balanceAmount.doubleValue())) {
                newBalance = balanceAmount.doubleValue() - transactionAmount.doubleValue();
                currentUserBalance.setAmount(newBalance);
                feignBalanaceConsumer.updateBalance(currentUserBalance, accountId, currentUserBalance.get_id(), headerUserId);
                logger.info("Amount debited");
                return new ResponseEntity<>(transactionService.createTransaction(transactionDto, accountId), HttpStatus.CREATED);
            } else if (transactionDto.getType().equals(Type.CREDIT)) {
                newBalance = balanceAmount.doubleValue() + transactionAmount.doubleValue();
                currentUserBalance.setAmount(newBalance);
                feignBalanaceConsumer.updateBalance(currentUserBalance, accountId, currentUserBalance.get_id(), headerUserId);
                logger.info("Amount credited");
                return new ResponseEntity<>(transactionService.createTransaction(transactionDto, accountId), HttpStatus.CREATED);
            } else {
                logger.info("Insufficient balance");
                throw new TransactionAmountException("Insufficient balance");
            }
        }
        else {
            throw new AccountIdMismatchException("Account ID " + accountId + " is not available for customer");
        }
    }

    @GetMapping("/accounts/{accountId}/transactions")
    public List<TransactionDto> getAllTransactionByAccountId(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                                             @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize,
                                                             @PathVariable("accountId") String accountId,
                                                             @RequestHeader(value = "userid") String headerUserId) {
        AccountDto accountDto = feignAccountConsumer.getAccount(headerUserId, accountId, headerUserId);
        if(accountDto != null) {
            return transactionService.getTransactionByAccountId(page, pageSize, accountId);
        }
        else {
            throw new AccountIdMismatchException(MessageConstant.NOT_AUTHORIZED_USER);
        }
    }

    @GetMapping("/accounts/{accountId}/transactions/{transactionId}")
    public ResponseEntity<TransactionDto> getTransactionByAccountId(@PathVariable("accountId") String accountId,
                                                                    @PathVariable("transactionId") String transactionId,
                                                                    @RequestHeader(value = "userid") String headerUserId)
            throws TransactionIdNotFoundException, AccountIdMismatchException {
        AccountDto accountDto = feignAccountConsumer.getAccount(headerUserId, accountId, headerUserId);
        if(accountDto != null) {
            return new ResponseEntity<>(transactionService.getTransactionIdByAccountId(accountId, transactionId), HttpStatus.OK);
        }
        else {
            throw new AccountIdMismatchException(MessageConstant.NOT_AUTHORIZED_USER);
        }
    }

    @DeleteMapping("/accounts/{accountId}/transactions/{transactionId}")
    public ResponseEntity<String> deleteTransactionByAccountId(@PathVariable("accountId") String accountId,
                                                               @PathVariable("transactionId") String transactionId,
                                                               @RequestHeader(value = "userid") String headerUserId)
            throws TransactionIdNotFoundException, AccountIdMismatchException {
        AccountDto accountDto = feignAccountConsumer.getAccount(headerUserId, accountId, headerUserId);
        if(accountDto != null) {
            transactionService.deleteTransactionIdByAccountId(accountId, transactionId);
            return new ResponseEntity<>("Transaction deleted successfully", HttpStatus.OK);
        }
        else {
            throw new AccountIdMismatchException(MessageConstant.NOT_AUTHORIZED_USER);
        }
    }

    @DeleteMapping("/accounts/{accountId}/transactions")
    public ResponseEntity<String> deleteAllTransactionsByAccountId(@PathVariable("accountId") String accountId,
                                                               @RequestHeader(value = "userid") String headerUserId)
            throws AccountIdMismatchException {
        AccountDto accountDto = feignAccountConsumer.getAccount(headerUserId, accountId, headerUserId);
        if(accountDto != null) {
            transactionService.deleteAllTransactionsByAccountId(accountId);
            return new ResponseEntity<>("Transactions deleted successfully", HttpStatus.OK);
        }
        else {
            throw new AccountIdMismatchException(MessageConstant.NOT_AUTHORIZED_USER);
        }
    }

}

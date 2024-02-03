package com.maveric.accountservice.controller;

import com.maveric.accountservice.constant.MessageConstant;
import com.maveric.accountservice.dto.AccountDto;
import com.maveric.accountservice.dto.BalanceDto;
import com.maveric.accountservice.dto.UserDto;
import com.maveric.accountservice.entity.Account;
import com.maveric.accountservice.exception.AccountNotFoundException;
import com.maveric.accountservice.exception.CustomerIDNotFoundExistsException;
import com.maveric.accountservice.feignclient.FeignBalanceService;
import com.maveric.accountservice.feignclient.FeignTransactionService;
import com.maveric.accountservice.feignclient.FeignUserConsumer;
import com.maveric.accountservice.repository.AccountRepository;
import com.maveric.accountservice.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


import com.maveric.accountservice.exception.CustomerIdMissmatchException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import java.util.List;
import java.util.Optional;


@RestController

@RequestMapping("/api/v1")
public class AccountController {
    @Autowired
    AccountService accountService;
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    FeignUserConsumer feignUserConsumer;

    @Autowired
    FeignBalanceService feignBalanceService;

    @Autowired
    FeignTransactionService feignTransactionService;


    @GetMapping("customers/{customerId}/accounts/{accountId}")
    public AccountDto getAccount(@PathVariable("customerId") String customerId,
                                 @Valid @PathVariable("accountId") String accountId,
                                 @RequestHeader(value = "userid") String headerUserId) throws AccountNotFoundException ,CustomerIDNotFoundExistsException{
        if(headerUserId.equals(customerId)) {
            AccountDto accountDto = accountService.getAccountByAccId(customerId, accountId);
            ResponseEntity<BalanceDto> balanceDto = feignBalanceService.getAllBalanceByAccountId(accountId, customerId);
            accountDto.setBalanceDto(balanceDto.getBody());
            return new ResponseEntity<>(accountDto, HttpStatus.OK).getBody();
        } else {
            throw new CustomerIdMissmatchException(MessageConstant.NOT_AUTHORIZED_USER);
        }
    }

    @PutMapping("customers/{customerId}/accounts/{accountId}")
    public ResponseEntity<Account> updateAccount(@PathVariable(name = "customerId") String customerId,
                                                 @Valid @PathVariable(name = "accountId") String accountId,
                                                 @RequestBody AccountDto account,
                                                 @RequestHeader(value = "userid") String headerUserId) {

        if(headerUserId.equals(customerId)) {
            Account account1 = accountService.updateAccount(customerId, accountId, account);

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("message","Account successfully updated");
            return new ResponseEntity<>(account1, responseHeaders, HttpStatus.OK);
        } else {
            throw new CustomerIdMissmatchException(MessageConstant.NOT_AUTHORIZED_USER);
        }

    }

    @GetMapping("customers/{customerId}/accounts")
    public ResponseEntity<List<AccountDto>> getAccountByCustomerId(@PathVariable String customerId,
                                                                   @Valid @RequestParam(defaultValue = "0") Integer page,
                                                                   @RequestParam(defaultValue = "10") @Valid Integer pageSize,
                                                                   @RequestHeader(value = "userid") String headerUserId) throws CustomerIdMissmatchException {

        if(headerUserId.equals(customerId)) {
            List<AccountDto> accountDtoResponse = accountService.getAccountByUserId(page, pageSize, customerId);
            return new ResponseEntity<>(accountDtoResponse, HttpStatus.OK);
        } else {
            throw new CustomerIdMissmatchException("You are not an authorized user");
        }
    }

    @DeleteMapping("customers/{customerId}/accounts/{accountId}")
    public ResponseEntity<String> deleteAccount(@PathVariable String customerId,@Valid
                                                @PathVariable String accountId,
                                                @RequestHeader(value = "userid") String headerUserId) throws AccountNotFoundException,CustomerIdMissmatchException{

        if(headerUserId.equals(customerId)) {
            if(accountRepository.findById(accountId).isPresent()) {
                feignBalanceService.deleteBalanceByAccountId(accountId, headerUserId);
                feignTransactionService.deleteTransactionByAccountId(accountId, headerUserId);
                accountService.deleteAccount(accountId,customerId);
                return new ResponseEntity<>("Account deleted successfully", HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>("Account not found", HttpStatus.NOT_FOUND);
            }
        } else {
            throw new CustomerIdMissmatchException("You are not an authorized user");
        }
    }


    @PostMapping("customers/{customerId}/accounts")
    public ResponseEntity<AccountDto> createAccount (@PathVariable String customerId, @Valid @RequestBody AccountDto
            accountDto, @RequestHeader(value = "userid") String headerUserId){

        if(headerUserId.equals(customerId)) {
            UserDto userDto = feignUserConsumer.getUserById(customerId, headerUserId).getBody(); //NOSONAR
            if(userDto.getId().equals(accountDto.getCustomerId())) {  //NOSONAR
                AccountDto accountDtoResponse = accountService.createAccount(customerId, accountDto);
                return new ResponseEntity<>(accountDtoResponse, HttpStatus.CREATED);
            } else {
                throw new CustomerIDNotFoundExistsException("Customer does not exist");
            }
        } else {
            throw new CustomerIdMissmatchException(MessageConstant.NOT_AUTHORIZED_USER);
        }


    }

    @DeleteMapping("customers/{customerId}/accounts")
    public ResponseEntity<String> deleteAllAccount(@PathVariable String customerId,
                                                @RequestHeader(value = "userid") String headerUserId) throws AccountNotFoundException,CustomerIdMissmatchException{

        if(headerUserId.equals(customerId)) {
        List<Account> accountList = accountRepository.findAccountsByCustomerId(customerId);
            accountList.forEach(account -> {
                feignBalanceService.deleteBalanceByAccountId(account.get_id(), account.getCustomerId());
                feignTransactionService.deleteTransactionByAccountId(account.get_id(), account.getCustomerId());
                accountService.deleteAccount(account.get_id(), account.getCustomerId());
            });
            return new ResponseEntity<>(MessageConstant.DELETION_SUCCESS, HttpStatus.OK);
        } else {
            throw new CustomerIdMissmatchException(MessageConstant.NOT_AUTHORIZED_USER);
        }
    }

    @GetMapping("customers/customerId/accounts/{accountId}")
    public AccountDto getAccountByUserId(@PathVariable("accountId") String accountId,
                                         @RequestHeader(value = "userid") String headerUserId) throws AccountNotFoundException ,CustomerIDNotFoundExistsException {
        Optional<Account> account = accountRepository.findById(accountId);
        if (account.isPresent()) {
            if (account.get().getCustomerId().equals(headerUserId)) {
                return accountService.getAccountByAccId(headerUserId, accountId);
            } else {
                throw new CustomerIDNotFoundExistsException(MessageConstant.NOT_AUTHORIZED_USER);
            }
        } else {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
    }
}



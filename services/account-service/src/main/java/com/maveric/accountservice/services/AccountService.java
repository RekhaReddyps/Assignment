package com.maveric.accountservice.services;

import com.maveric.accountservice.dto.AccountDto;
import com.maveric.accountservice.entity.Account;

import com.maveric.accountservice.exception.AccountNotFoundException;

import com.maveric.accountservice.exception.CustomerIdMissmatchException;

import com.maveric.accountservice.exception.CustomerIDNotFoundExistsException;

import java.util.List;


public interface AccountService {

    AccountDto getAccountByAccId(String customerId, String accountId) throws AccountNotFoundException,CustomerIDNotFoundExistsException;



    List<Account> getAccountById(String customerId);
    public List<AccountDto> getAccountByUserId(Integer page, Integer pageSize, String customerId)throws CustomerIdMissmatchException;

    Account updateAccount(String customerId, String accountId, AccountDto account) throws CustomerIDNotFoundExistsException;

    Object updateAccount(Object any);


    public String deleteAccount(String accountId,String customerID)throws CustomerIdMissmatchException;


    public AccountDto createAccount(String customerId, AccountDto accountDto);

}



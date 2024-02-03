package com.maveric.accountservice.services;

import com.maveric.accountservice.dto.AccountDto;
import com.maveric.accountservice.entity.Account;
import com.maveric.accountservice.exception.*;

import com.maveric.accountservice.mapper.AccountMapper;

import com.maveric.accountservice.repository.AccountRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.maveric.accountservice.enums.Constants.ACCOUNT_NOT_FOUND_MESSAGE;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired

    AccountRepository accountRepository;

    @Autowired
    private AccountMapper mapper;
    @Override
    public AccountDto getAccountByAccId(String customerId, String accountId) throws AccountNotFoundException,CustomerIDNotFoundExistsException {
        Account account = accountRepository.findById(accountId).orElseThrow(
                () -> new AccountNotFoundException("Account details not found")
        );
        if (customerId.equals(account.getCustomerId())) {
            return mapper.map(account);
        } else {
            throw new CustomerIDNotFoundExistsException("Customer Id not available");
        }

    }

    @Override
    public List<Account> getAccountById(String customerId) {
        return getAccountById("1");
    }

    @Override
    public String deleteAccount(String accountId,String customerId) throws AccountNotFoundException,CustomerIdMissmatchException{
        Account account = accountRepository.findById(accountId).orElseThrow(
                () -> new AccountIDNotfoundException("Account ID not available")
        );
        if (customerId.equals(account.getCustomerId())) {
            accountRepository.deleteById(accountId);
        } else {
            throw new CustomerIdMissmatchException("Customer ID not available");
        }
        return "Account deleted sucessfully";


    }

    @Override
    public Account updateAccount(String customerId, String accountId, AccountDto account) {
        Account accountResult = accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException(ACCOUNT_NOT_FOUND_MESSAGE + accountId));
        if (!customerId.equals(account.getCustomerId())) {
            throw new CustomerIDNotFoundExistsException("Customer Id should not be empty");
        }

        accountResult.set_id(accountResult.get_id());


        accountResult.setCustomerId(account.getCustomerId());
        accountResult.setType(account.getType());
        accountResult.setCreatedAt(accountResult.getCreatedAt());
        accountResult.setUpdatedAt(account.getUpdatedAt());
        Account accountUpdated = accountRepository.save(accountResult);
        return accountRepository.save(accountUpdated);
    }

    @Override
    public Object updateAccount(Object any) {
        return any;
    }


    @Override
    public AccountDto createAccount(String customerId, AccountDto accountDto) {
        if (customerId.equalsIgnoreCase(accountDto.getCustomerId())) {


            Account account = mapper.map(accountDto);
            Account accountResult = accountRepository.save(account);
            return mapper.map(accountResult);
        } else {
            log.error("Customer not found! Cannot create Account.");
            throw new PathParamsVsInputParamsMismatchException("Customer Id-" + accountDto.getCustomerId()  +  "Missmatch");
        }

    }

    @Override
    public List<AccountDto> getAccountByUserId(Integer page, Integer pageSize, String customerId) throws
            CustomerIdMissmatchException {
        Pageable paging = PageRequest.of(page, pageSize);
        Page<Account> pageResult = accountRepository.findByCustomerId(paging, customerId);
        if (pageResult.hasContent()) {
            List<Account> account = pageResult.getContent();
            log.info("Retrieved list of accounts from DB");
            return mapper.mapToDto(account);
        } else {
            throw new CustomerIdMissmatchException("Customer Id Missmatch");
        }
    }

}


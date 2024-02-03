package com.maveric.accountservice.mapper;

import com.maveric.accountservice.dto.AccountDto;
import com.maveric.accountservice.entity.Account;

import java.util.List;

//@Mapper(componentModel="Account")


public interface AccountMapper {
    Account map(AccountDto accountDto);

    AccountDto map(Account account);

    List<Account> mapToModel (List<AccountDto> accounts);


    List<AccountDto> mapToDto (List<Account> accounts);

    byte[] writeValueAsString(AccountDto accountDto);
}



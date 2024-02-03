package com.maveric.accountservice.service;

import com.maveric.accountservice.dto.AccountDto;
import com.maveric.accountservice.entity.Account;
import com.maveric.accountservice.exception.AccountIDNotfoundException;
import com.maveric.accountservice.exception.AccountNotFoundException;
import com.maveric.accountservice.exception.PathParamsVsInputParamsMismatchException;
import com.maveric.accountservice.mapper.AccountMapperImpl;
import com.maveric.accountservice.repository.AccountRepository;
import com.maveric.accountservice.services.AccountServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.maveric.accountservice.AccountServiceApplicationTests.getAccount;
import static com.maveric.accountservice.AccountServiceApplicationTests.getAccountDto;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
//@RunWith(SpringRunner.class)
class AccountServiceImplTest {
    @InjectMocks
    private AccountServiceImpl service;

    @Mock
    private AccountRepository repository;

    @Mock
    private AccountMapperImpl mapper;

    @Mock
    private Page pageResult;
    @Test
    void getAccountDetailsById() throws AccountNotFoundException {
        when(repository.findById(any(String.class))).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class,()->{
            service.getAccountByAccId("1","1");
        });
    }

    @Test
    void testGetAccounts() {
        Page<Account> pagedResponse = new PageImpl(Arrays.asList(getAccount(),getAccount()));
        when(repository.findByCustomerId(any(Pageable.class),any())).thenReturn(pagedResponse);
        when(mapper.mapToDto(any())).thenReturn(Arrays.asList(getAccountDto(),getAccountDto()));
        List<AccountDto> accountDtos= service.getAccountByUserId(1, 2,"1");
        assertEquals(accountDtos.get(0).getType(), getAccountDto().getType());
    }

    @Test
    void createAccount() {
        when(mapper.map(any(AccountDto.class))).thenReturn(getAccount());
        when(mapper.map(any(Account.class))).thenReturn(getAccountDto());
        when(repository.save(any())).thenReturn(getAccount());

        AccountDto transactionDto = service.createAccount("1234",getAccountDto());

        assertSame(transactionDto.getCustomerId(), getAccount().getCustomerId());
    }

    @Test
    void createAccount_failure() {
        Throwable error = assertThrows(PathParamsVsInputParamsMismatchException.class,()->service.createAccount("1233",getAccountDto()));  //NOSONAR
        assertEquals("Customer Id-1234Missmatch",error.getMessage());
    }
    @Test
    void deleteAccount() {
        when(repository.findById("123")).thenReturn(Optional.of(getAccount()));
        String accounDto = service.deleteAccount("123","1234");

        assertSame( "Account deleted sucessfully",accounDto);

    }
    @Test
    void deleteAccount_failure() {
        Throwable error = assertThrows(AccountIDNotfoundException.class,()->service.deleteAccount("1234","1234"));
        assertEquals("Account ID not available",error.getMessage());
    }

}


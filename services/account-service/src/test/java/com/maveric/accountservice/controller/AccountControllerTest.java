package com.maveric.accountservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maveric.accountservice.dto.AccountDto;
import com.maveric.accountservice.dto.UserDto;
import com.maveric.accountservice.entity.Account;
import com.maveric.accountservice.enums.Gender;
import com.maveric.accountservice.enums.Type;
import com.maveric.accountservice.feignclient.FeignBalanceService;
import com.maveric.accountservice.feignclient.FeignTransactionService;
import com.maveric.accountservice.feignclient.FeignUserConsumer;
import com.maveric.accountservice.repository.AccountRepository;
import com.maveric.accountservice.services.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @MockBean
    AccountRepository accountRepository;

    @MockBean
    AccountService accountService;

    @MockBean
    FeignTransactionService feignTransactionService;

    @MockBean
    FeignUserConsumer feignUserConsumer;

    @MockBean
    FeignBalanceService feignBalanceService;

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    private static final String API_V1_ACCOUNTS = "/api/v1/customers/1234/accounts";

    @Test
    void getAccountByCustomerId() throws Exception{
        mvc.perform(get(API_V1_ACCOUNTS + "?page=0&pageSize=2").header("userid", "1234"))
                .andExpect(status().isOk()).andDo(print());
    }

    @Test
    void shouldThrowErrorWhenNotAuthorizedUserForDeleteAccount() throws Exception{
        mvc.perform(delete(API_V1_ACCOUNTS + "/" + "1L").header("userid", "123"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void shouldThrowErrorWhenCreateAccountCustomerIDMismatch() throws Exception{
        when(feignUserConsumer.getUserById(anyString(), anyString())).thenReturn(getUserDto());
        mvc.perform(post(API_V1_ACCOUNTS).contentType(MediaType.APPLICATION_JSON)
                .header("userid", "1234")
                .content(objectMapper.writeValueAsString(getAccountDtoData())))
                .andExpect(status().isBadRequest()).andDo(print());
    }

    @Test
    void shouldThrowErrorWhenDeleteAllAccountNotAuthorizedUser() throws Exception{
        mvc.perform(delete(API_V1_ACCOUNTS + "/" + "1L").header("userid", "123"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }


    public static ResponseEntity<Account> getAccountData(){
        Account account = new Account();
        account.set_id("1L");
        account.setType(Type.CURRENT);
        account.setCustomerId("1234");
        account.setCreatedAt(Date.from(Instant.parse("2023-02-01T00:00:00Z")));
        return ResponseEntity.status(HttpStatus.OK).body(account);
    }

    public static ResponseEntity<AccountDto> getAccountDtoData(){
        AccountDto account = new AccountDto();
        account.set_id("1L");
        account.setType(Type.CURRENT);
        account.setCustomerId("1234");
        account.setCreatedAt(Date.from(Instant.parse("2023-02-01T00:00:00Z")));
        return ResponseEntity.status(HttpStatus.OK).body(account);
    }

    public static ResponseEntity<UserDto> getUserDto() {
        UserDto user = new UserDto();
        user.setId("1234");
        user.setFirstName("Hins");
        user.setMiddleName("D");
        user.setLastName("Jain");
        user.setAddress("Mumbai");
        user.setGender(Gender.MALE);
        user.setEmail("hinsj@maveric-systems.com");
        user.setDateOfBirth(Date.from(Instant.parse("1994-10-27T00:00:00Z")));
        user.setPhoneNumber("9594484384");

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
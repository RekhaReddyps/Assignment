package com.maveric.transactionservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maveric.transactionservice.constant.Currency;
import com.maveric.transactionservice.dto.BalanceDto;
import com.maveric.transactionservice.exception.AccountIdMismatchException;
import com.maveric.transactionservice.feignclient.FeignAccountConsumer;
import com.maveric.transactionservice.feignclient.FeignBalanaceConsumer;
import com.maveric.transactionservice.repository.TransactionRepository;
import com.maveric.transactionservice.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static com.maveric.transactionservice.TransactionServiceApplicationTests.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = TransactionController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class TransactionControllerTest {


    public static final String API_V1_ACCOUNTS ="/api/v1/accounts/1234/transactions";
    public static final String API_V1_NOT_ACCOUNTS ="/api/v1/accounts/123/transactions";

    @MockBean
    private TransactionRepository transactionRepository;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    FeignAccountConsumer feignAccountConsumer;

    @MockBean
    FeignBalanaceConsumer feignBalanaceConsumer;

    @Test
    void shouldThrowErrorWhenWrongDataPassedForCreateUser() throws Exception{
        mockMvc.perform(post(API_V1_ACCOUNTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getTransactionInvalidData()))
                        .header("userid", "1L")).andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void shouldCreateUser() throws Exception{
        when(feignAccountConsumer.getAccount(anyString(), anyString(), anyString())).thenReturn(getAccountDto());
        when(feignBalanaceConsumer.getAllBalanceByAccountId(anyString(), anyString())).thenReturn(getSampleBalance());
        mockMvc.perform(post(API_V1_ACCOUNTS)
                        .contentType(MediaType.APPLICATION_JSON).header("userid", "1234")
                        .content(objectMapper.writeValueAsString(getTransactionDto()))).andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    void shouldThrowErrorWhenAccountIdNotFoundForCreateUser() throws Exception{
        when(feignAccountConsumer.getAccount(anyString(), anyString(), anyString())).thenThrow(AccountIdMismatchException.class);
        mockMvc.perform(post(API_V1_NOT_ACCOUNTS)
                        .contentType(MediaType.APPLICATION_JSON).header("userid", "1234")
                        .content(objectMapper.writeValueAsString(getTransactionDto()))).andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void getTransactionByAccountId() throws Exception{
        when(feignAccountConsumer.getAccount(anyString(), anyString(), anyString())).thenReturn(getAccountDto());
        mockMvc.perform(get(API_V1_ACCOUNTS +"/1234")
                        .contentType(MediaType.APPLICATION_JSON).header("userid", "1234"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void getAllTransactionByAccountId() throws Exception{
        when(feignAccountConsumer.getAccount(anyString(), anyString(), anyString())).thenReturn(getAccountDto());
        mockMvc.perform(get(API_V1_ACCOUNTS)
                        .contentType(MediaType.APPLICATION_JSON).header("userid", "1234"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void shouldThrowErrorWhenHeaderIdIsDifferentForGetAllTransactionByAccountId() throws Exception{
       mockMvc.perform(get(API_V1_ACCOUNTS + "?page=0&pageSize=2").header("userid", "1234"))
               .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void shouldThrowErrorWhenHeaderIdIsDifferentForgetTransactionByAccountId() throws Exception{
       mockMvc.perform(get(API_V1_ACCOUNTS + "/1234").header("userid", "1234"))
               .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void shouldThrowErrorWhenHeaderIdIsDifferentForDeleteTransactionByAccountId() throws Exception{
        mockMvc.perform(delete(API_V1_ACCOUNTS + "/1234").header("userid", "1234"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void shouldThrowErrorWhenHeaderIdIsDifferentFordeleteAllTransactionsByAccountId() throws Exception{
        mockMvc.perform(delete(API_V1_ACCOUNTS + "/1234").header("userid", "1234"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void shouldDeleteAllTransactionsByAccountId() throws Exception{
        when(feignAccountConsumer.getAccount(anyString(), anyString(), anyString())).thenReturn(getAccountDto());
        mockMvc.perform(delete(API_V1_ACCOUNTS).header("userid", "1234"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void shouldThrowErrorDeleteAllTransactionsByAccountId() throws Exception{
        when(feignAccountConsumer.getAccount(anyString(), anyString(), anyString())).thenThrow(AccountIdMismatchException.class);
        mockMvc.perform(delete(API_V1_NOT_ACCOUNTS).header("userid", "1234"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void shouldDeleteTransactionsByTransactionIdAndAccountId() throws Exception{
        when(feignAccountConsumer.getAccount(anyString(), anyString(), anyString())).thenReturn(getAccountDto());
        mockMvc.perform(delete(API_V1_ACCOUNTS+"/1234").header("userid", "1234"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    public ResponseEntity<BalanceDto> getSampleBalance(){
        BalanceDto balance = new BalanceDto();
        balance.setAmount(10000);
        balance.setCurrency(Currency.INR);
        balance.setAccountId("1234");
        return ResponseEntity.status(HttpStatus.OK).body(balance);
    }
}
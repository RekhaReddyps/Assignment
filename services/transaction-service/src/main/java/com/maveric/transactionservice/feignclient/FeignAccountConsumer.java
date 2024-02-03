package com.maveric.transactionservice.feignclient;

import com.maveric.transactionservice.dto.AccountDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "account-service")
public interface FeignAccountConsumer {
    @GetMapping("api/v1/customers/{customerId}/accounts/{accountId}")
    public AccountDto getAccount(@PathVariable("customerId") String customerId,
                                 @Valid @PathVariable("accountId") String accountId,
                                 @RequestHeader(value = "userid") String headerUserId);
}

package com.maveric.accountservice.feignclient;

import com.maveric.accountservice.dto.BalanceDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@FeignClient(value = "balance-service")
public interface FeignBalanceService {

    @DeleteMapping("api/v1/accounts/{accountId}/balances")
    public ResponseEntity<String> deleteBalanceByAccountId(@PathVariable("accountId") String accountId,
                                                           @RequestHeader(value = "userid") String headerUserId);

    @GetMapping("api/v1/accounts/{accountId}/balances")
    public ResponseEntity<BalanceDto> getAllBalanceByAccountId(@PathVariable("accountId") @Valid String accountId,
                                                               @RequestHeader(value = "userid") String headerUserId);
}

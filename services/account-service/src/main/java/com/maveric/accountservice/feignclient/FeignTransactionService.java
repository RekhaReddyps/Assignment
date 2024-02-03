package com.maveric.accountservice.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "transaction-service")
public interface FeignTransactionService {

    @DeleteMapping("api/v1/accounts/{accountId}/transactions")
    public ResponseEntity<String> deleteTransactionByAccountId(@PathVariable("accountId") String accountId,
                                                               @RequestHeader(value = "userid") String headerUserId);
}

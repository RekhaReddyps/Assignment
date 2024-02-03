package com.maveric.transactionservice.feignclient;

import com.maveric.transactionservice.dto.BalanceDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "balance-service")
public interface FeignBalanaceConsumer {

    @GetMapping("api/v1/accounts/{accountId}/balances/{balanceId}")
    public ResponseEntity<BalanceDto> getBalanceByAccountId(@PathVariable("accountId") String accountId,
                                                            @PathVariable("balanceId") String balanceId,
                                                            @RequestHeader(value = "userid") String headerUserId);

    @GetMapping("api/v1/accounts/{accountId}/balances")
    public ResponseEntity<BalanceDto> getAllBalanceByAccountId(@PathVariable("accountId") @Valid String accountId,
                                                               @RequestHeader(value = "userid") String headerUserId);

    @PutMapping("api/v1/accounts/{accountId}/balances/{balanceId}")
    public ResponseEntity<BalanceDto> updateBalance(@Valid @RequestBody BalanceDto balanceDto,
                                                    @PathVariable String accountId, @PathVariable String balanceId,
                                                    @RequestHeader(value = "userid") String headerUserId);

}

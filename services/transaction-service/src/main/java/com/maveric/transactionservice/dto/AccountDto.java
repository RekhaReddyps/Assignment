package com.maveric.transactionservice.dto;

import com.maveric.transactionservice.constant.AccountType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {

    private String _id;

    private AccountType type;

    @Enumerated(EnumType.STRING)
    private String customerId;
}

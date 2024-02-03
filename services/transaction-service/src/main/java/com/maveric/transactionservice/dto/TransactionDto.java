package com.maveric.transactionservice.dto;

import com.maveric.transactionservice.constant.Type;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionDto {
    private String _id;

    @NotEmpty(message = "Please enter account ID")
    private String accountId;

    @Min(value = 1, message = "Amount cannot be less then 1")
    private Number amount;

    @Enumerated(EnumType.STRING)
    private Type type;

    private LocalDateTime createdAt = LocalDateTime.now();
}

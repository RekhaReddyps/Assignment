package com.maveric.accountservice.dto;

import lombok.*;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import java.util.Currency;


    @Getter
    @Setter

    @AllArgsConstructor
    @NoArgsConstructor
    public class Balance {
        @Id
        private String _id;
        private String amount;
        private Currency currency;
        @NotBlank(message = "AccountId is mandatory")
        private String accountId;
    }

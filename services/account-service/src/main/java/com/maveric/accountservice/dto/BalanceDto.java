package com.maveric.accountservice.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor

public class BalanceDto {
    private String  _id;
    private String accountId;
    private Number amount;
    private String currency;

    private Date createdAt;
    private Date updatedAt;


}

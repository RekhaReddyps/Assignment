package com.maveric.accountservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.maveric.accountservice.enums.Type;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.util.Date;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto{
    private String _id;
    @NotNull(message = "Type is mandatory - 'SAVINGS' or 'CURRENT'")
    private Type type;

    @NotNull(message = "Customer Id is mandatory")
    private String customerId;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date createdAt=new Date();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = true)
    private Date updatedAt =new Date();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BalanceDto balanceDto;
}
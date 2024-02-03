package com.maveric.gatewayservice.Model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionResponseModel {

    private String code;
    private String message;
}

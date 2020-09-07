package com.gehling.agibank.exame.dto;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Seller {

    private String CPF;
    private String Name;
    private BigDecimal Salary;
    private BigDecimal valueSold;

}

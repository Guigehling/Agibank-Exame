package com.gehling.agibank.exame.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Customer {

    private String CNPJ;
    private String Name;
    private String Businessarea;
}

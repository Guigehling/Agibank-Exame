package com.gehling.agibank.exame.dto;

import lombok.*;

import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ImportedFile {

    private ArrayList<Customer> customerList;
    private ArrayList<Salesman> salesmenList;
    private ArrayList<Sale> saleList;

}

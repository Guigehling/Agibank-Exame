package com.gehling.agibank.exame.dto;

import lombok.*;

import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FileImportedContent {

    private ArrayList<Customer> customerList;
    private ArrayList<Seller> sellersList;
    private ArrayList<Sale> saleList;

}

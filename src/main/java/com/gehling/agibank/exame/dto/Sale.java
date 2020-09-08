package com.gehling.agibank.exame.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Sale {

    private Long idSale;
    private ArrayList<Item> item;
    private Seller seller;

    public BigDecimal getAmount() {
        BigDecimal saleAmount = BigDecimal.ZERO;
        for (Item item : this.item) {
            saleAmount = saleAmount.add(item.getPrice().multiply(item.getQuantity()));
        }
        return saleAmount;
    }

}

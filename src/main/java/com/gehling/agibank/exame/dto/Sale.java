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
    private ArrayList<ItemSold> itemSold;
    private Salesman salesman;

    public BigDecimal getAmount() {
        BigDecimal saleAmount = BigDecimal.ZERO;
        for (ItemSold itemSold : this.itemSold) {
            saleAmount = saleAmount.add(itemSold.getPrice().multiply(itemSold.getQuantity()));
        }
        return saleAmount;
    }

}

package com.gehling.agibank.exame.dto;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ItemSold {

    private Long idItem;
    private BigDecimal quantity;
    private BigDecimal price;

}

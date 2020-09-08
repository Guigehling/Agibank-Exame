package com.gehling.agibank.exame.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FileExportContent {

    private long customerAmount;
    private long sellersAmount;
    private Sale moreExpansiveSale;
    private Seller worstSeller;

    public String getCustomerAmountMessage() {
        return "Total de clientes: " + this.customerAmount;
    }

    public String getSellersAmountMessage() {
        return "Total de vendedores: " + this.sellersAmount;
    }

    public String getMoreExpansiveSaleMessage() {
        return "Identificador da venda de maior valor: " + this.moreExpansiveSale.getIdSale();
    }

    public String getWorstSellerMessage() {
        return "Pior vendedor: " + this.worstSeller.getName();
    }

}

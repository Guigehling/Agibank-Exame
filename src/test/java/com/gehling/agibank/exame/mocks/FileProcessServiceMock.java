package com.gehling.agibank.exame.mocks;

import com.gehling.agibank.exame.dto.FileImportedContent;
import com.gehling.agibank.exame.dto.Item;
import com.gehling.agibank.exame.dto.Sale;
import com.gehling.agibank.exame.dto.Seller;

import java.math.BigDecimal;
import java.util.ArrayList;

public class FileProcessServiceMock {

    public static FileImportedContent getListWithOneWrostSeller() {
        ArrayList<Item> moreExpansiveItemList = new ArrayList<>();
        ArrayList<Item> lessExpansiveItemList = new ArrayList<>();
        ArrayList<Seller> sellersList = new ArrayList<>();
        ArrayList<Sale> sales = new ArrayList<>();

        Seller bestSeller = Seller.builder()
                .Name("Vendedor_A")
                .CPF("530.281.480-30")
                .build();
        Seller wrostSeller = Seller.builder()
                .Name("Vendedor_B")
                .CPF("762.863.480-00")
                .build();

        sellersList.add(bestSeller);
        sellersList.add(wrostSeller);

        moreExpansiveItemList.add(Item.builder()
                .price(BigDecimal.valueOf(100))
                .quantity(BigDecimal.valueOf(5))
                .build());
        lessExpansiveItemList.add(Item.builder()
                .price(BigDecimal.valueOf(2))
                .quantity(BigDecimal.valueOf(5))
                .build());

        sales.add(Sale.builder()
                .seller(bestSeller)
                .item(moreExpansiveItemList)
                .build());
        sales.add(Sale.builder()
                .seller(wrostSeller)
                .item(lessExpansiveItemList)
                .build());

        return FileImportedContent.builder()
                .saleList(sales)
                .sellersList(sellersList)
                .build();
    }

}

package com.gehling.agibank.exame.service;

import com.gehling.agibank.exame.dto.FileExportContent;
import com.gehling.agibank.exame.dto.FileImportedContent;
import com.gehling.agibank.exame.dto.Sale;
import com.gehling.agibank.exame.dto.Seller;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

import static java.util.Comparator.comparing;

@Service
public class FileProcessService {

    public FileExportContent mountInfoToExport(FileImportedContent fileImportedContent) {
        return FileExportContent.builder()
                .customerAmount(fileImportedContent.getCustomerList().size())
                .sellersAmount(fileImportedContent.getSellersList().size())
                .moreExpansiveSale(getMoreExpensiveSale(fileImportedContent.getSaleList()))
                .worstSeller(identifyTheWorstSeller(fileImportedContent))
                .build();
    }

    private Sale getMoreExpensiveSale(ArrayList<Sale> sales) {
        Optional<Sale> moreExpansiveSale = sales.stream().max(comparing(Sale::getAmount));
        return moreExpansiveSale.orElse(null);
    }

    public Seller identifyTheWorstSeller(FileImportedContent fileImportedContent) {
        fileImportedContent.getSellersList().forEach(seller -> {
            fileImportedContent.getSaleList().forEach(sale -> {
                if (sale.getSeller().getCPF().equals(seller.getCPF())) {
                    if (Objects.isNull(seller.getValueSold())) seller.setValueSold(BigDecimal.ZERO);
                    seller.setValueSold(seller.getValueSold().add(sale.getAmount()));
                }
            });
        });

        Optional<Seller> salesmanOptional = fileImportedContent.getSellersList().stream().min(comparing(Seller::getValueSold));
        return salesmanOptional.orElse(null);
    }

}

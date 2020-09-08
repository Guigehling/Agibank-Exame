package com.gehling.agibank.exame.service;

import com.gehling.agibank.exame.dto.*;
import com.gehling.agibank.exame.exception.ProcessFileException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Service
public class FileConvertService {

    private static final String STANDARD_FILE_CONTENT_SPLITTER = "ç";
    private static final String STANDARD_ITEM_CONTENT_SPLITTER = "-";
    private static final String STANDARD_ITENS_SALES_SPLITTER = ",";
    private static final String START_ITENS_SALES_CONTENT = "[";
    private static final String END_ITENS_SALES_CONTENT = "]";

    private static final String CUSTOMER_IDENTIFIER_LINE = "001";
    private static final String SALESMAN_IDENTIFIER_LINE = "002";
    private static final String SALE_IDENTIFIER_LINE = "003";

    public FileImportedContent getFileContent(File file) throws ProcessFileException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return splitFileContent(reader);
        } catch (ProcessFileException e) {
            throw e;
        } catch (IOException e) {
            throw new ProcessFileException("Erro ao iniciar leitura do arquivo " + file.getPath(), e);
        }
    }

    private FileImportedContent splitFileContent(BufferedReader reader) throws ProcessFileException {
        ArrayList<Customer> customers = new ArrayList<>();
        ArrayList<Seller> salesmens = new ArrayList<>();
        ArrayList<Sale> sales = new ArrayList<>();

        try {
            while (reader.ready()) {
                String[] splitedLine = reader.readLine().split(STANDARD_FILE_CONTENT_SPLITTER);

                if (splitedLine[0].equals(CUSTOMER_IDENTIFIER_LINE)) {
                    salesmens.add(Seller.builder()
                            .CPF(splitedLine[1])
                            .Name(splitedLine[2])
                            .Salary(new BigDecimal(splitedLine[3]))
                            .valueSold(BigDecimal.ZERO)
                            .build());
                }

                if (splitedLine[0].equals(SALESMAN_IDENTIFIER_LINE)) {
                    customers.add(Customer.builder()
                            .CNPJ(splitedLine[1])
                            .Name(splitedLine[2])
                            .Businessarea(splitedLine[3])
                            .build());
                }

                if (splitedLine[0].equals(SALE_IDENTIFIER_LINE)) {
                    sales.add(Sale.builder()
                            .idSale(new Long(splitedLine[1]))
                            .item(splitSaleItems(splitedLine[2]))
                            .seller(mountSalesman(salesmens, splitedLine[3]))
                            .build());
                }
            }

            return FileImportedContent.builder()
                    .customerList(customers)
                    .sellersList(salesmens)
                    .saleList(sales)
                    .build();
        } catch (Exception e) {
            throw new ProcessFileException("Erro ao realizar o split do conteudo do arquivo.", e);
        }
    }

    private ArrayList<Item> splitSaleItems(String newItem) {
        ArrayList<Item> items = new ArrayList<>();

        String[] itensSplited = newItem
                .replace(START_ITENS_SALES_CONTENT, "")
                .replace(END_ITENS_SALES_CONTENT, "")
                .split(STANDARD_ITENS_SALES_SPLITTER);

        Arrays.stream(itensSplited).forEach(item -> {
            String[] itemSplited = item.split(STANDARD_ITEM_CONTENT_SPLITTER);
            items.add(Item.builder()
                    .idItem(new Long(itemSplited[0]))
                    .quantity(new BigDecimal(itemSplited[1]))
                    .price(new BigDecimal(itemSplited[2]))
                    .build());
        });
        return items;
    }

    private Seller mountSalesman(ArrayList<Seller> salesmens, String name) {
        Optional<Seller> optionalSalesman = salesmens.stream()
                .filter(salesman -> salesman.getName().equals(name))
                .findFirst();
        return optionalSalesman.orElse(null);
    }

}

package com.gehling.agibank.exame.service;

import com.gehling.agibank.exame.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

@Service
public class ReadingService {

    @Value("${file.import.source}")
    private String source;
    @Value("${file.export.target}")
    private String target;

    private static final String FILE_FORMAT = "\\w*\\.TXT";
    private static final String CUSTOMER_IDENTIFIER = "001";
    private static final String SALESMAN_IDENTIFIER = "002";
    private static final String SALE_IDENTIFIER = "003";

    public void readNewImportedFile() throws IOException {
        File directory = new File(this.source);
        if (directory.exists() && directory.isDirectory()) {
            for (String file : Objects.requireNonNull(directory.list())) {
                if (file.toUpperCase().matches(FILE_FORMAT)) {
                    ProcessImportedFile(file);
                }
            }
        }
    }

    private void ProcessImportedFile(String fileName) {
        File originalFile = new File(this.source + fileName);
        try (BufferedReader lerArquivo = new BufferedReader(new FileReader(originalFile))) {
            ImportedFile importedFile = handleWithStringImported(lerArquivo);
            exportResult(importedFile, fileName);
            originalFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exportResult(ImportedFile importedFile, String fileName) {
        long customerAmount = importedFile.getCustomerList().size();
        long salesmanAmount = importedFile.getSalesmenList().size();
        Sale moreExpansiveSale = getMoreExpensiveSale(importedFile.getSaleList());
        Salesman pior = identifyTheWorstSeller(importedFile);

        try (BufferedWriter buffer = new BufferedWriter(new FileWriter(mountFileResultname(fileName)))) {
            buffer.append("Quantidade de clientes no arquivo de entrada=" + customerAmount);
            buffer.newLine();
            buffer.append("Quantidade de vendedores no arquivo de entrada=" + salesmanAmount);
            buffer.newLine();
            buffer.append("ID da venda mais cara=" + moreExpansiveSale.getIdSale());
            buffer.newLine();
            buffer.append("Pior vendedor=" + pior.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String mountFileResultname(String originFileName) {
        DateTimeFormatter formatterData = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String nameFiltred = originFileName.replace(".txt", "");
        return this.target + formatterData.format(LocalDateTime.now()) + "_" + nameFiltred + "_" + "result.txt";
    }

    private ImportedFile handleWithStringImported(BufferedReader newFile) throws IOException {
        ArrayList<Customer> customers = new ArrayList<>();
        ArrayList<Salesman> salesmens = new ArrayList<>();
        ArrayList<Sale> sales = new ArrayList<>();

        while (newFile.ready()) {
            String[] splitedLine = newFile.readLine().split("ç");

            if (splitedLine[0].equals(CUSTOMER_IDENTIFIER)) {
                salesmens.add(Salesman.builder()
                        .CPF(splitedLine[1])
                        .Name(splitedLine[2])
                        .Salary(new BigDecimal(splitedLine[3]))
                        .valueSold(BigDecimal.ZERO)
                        .build());
            }

            if (splitedLine[0].equals(SALESMAN_IDENTIFIER)) {
                customers.add(Customer.builder()
                        .CNPJ(splitedLine[1])
                        .Name(splitedLine[2])
                        .Businessarea(splitedLine[3])
                        .build());
            }

            if (splitedLine[0].equals(SALE_IDENTIFIER)) {
                sales.add(Sale.builder()
                        .idSale(new Long(splitedLine[1]))
                        .itemSold(mountItem(splitedLine[2]))
                        .salesman(findSalesman(salesmens, splitedLine[3]))
                        .build());
            }
        }

        return ImportedFile.builder()
                .customerList(customers)
                .salesmenList(salesmens)
                .saleList(sales)
                .build();
    }

    private Salesman findSalesman(ArrayList<Salesman> salesmens, String name) {
        Optional<Salesman> optionalSalesman = salesmens.stream()
                .filter(salesman -> salesman.getName().equals(name))
                .findFirst();
        return optionalSalesman.orElse(null);
    }

    private ArrayList<ItemSold> mountItem(String newItem) {
        ArrayList<ItemSold> itemSolds = new ArrayList<>();
        String[] itensSplited = newItem
                .replace("[", "")
                .replace("]", "")
                .split(",");

        for (String item : itensSplited) {
            String[] itemSplited = item.split("-");
            itemSolds.add(ItemSold.builder()
                    .idItem(new Long(itemSplited[0]))
                    .quantity(new BigDecimal(itemSplited[1]))
                    .price(new BigDecimal(itemSplited[2]))
                    .build());
        }
        return itemSolds;
    }

    private Sale getMoreExpensiveSale(ArrayList<Sale> sales) {
        Optional<Sale> moreExpansiveSale = sales.stream().max(Comparator.comparing(Sale::getAmount));
        return moreExpansiveSale.get();
    }

    private Salesman identifyTheWorstSeller(ImportedFile importedFile) {
        importedFile.getSalesmenList().forEach(salesman -> {
            importedFile.getSaleList().forEach(sale -> {
                if (sale.getSalesman().getCPF().equals(salesman.getCPF())) {
                    salesman.setValueSold(salesman.getValueSold().add(sale.getAmount()));
                }
            });
        });

        Optional<Salesman> salesmanOptional = importedFile.getSalesmenList().stream().min(Comparator.comparing(Salesman::getValueSold));
        return salesmanOptional.get();
    }
}


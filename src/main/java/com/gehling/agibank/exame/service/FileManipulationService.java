package com.gehling.agibank.exame.service;

import com.gehling.agibank.exame.dto.FileExportContent;
import com.gehling.agibank.exame.exception.ProcessFileException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

@Service
public class FileManipulationService {

    @Value("${file.import.source}")
    private String source;
    @Value("${file.export.target.result}")
    private String targetResult;
    @Value("${file.export.target.done}")
    private String targetDone;

    private static final String DEFAULT_FILE_EXTENSION = ".txt";
    private static final String DEAFULT_FILE_EXTENSION_REGEX = "\\w*\\.TXT";

    public File identifyDirectory() throws ProcessFileException {
        File directorySource = new File(this.source);
        if (directorySource.exists() && directorySource.isDirectory()) {
            return directorySource;
        }
        throw new ProcessFileException("Diretorio inexistente em " + this.source);
    }

    private void createAndValidateDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) directory.mkdir();
    }

    public ArrayList<File> findValidFiles(File directory) {
        ArrayList<File> files = new ArrayList<>();
        for (String fileName : Objects.requireNonNull(directory.list())) {
            if (fileName.toUpperCase().matches(DEAFULT_FILE_EXTENSION_REGEX))
                files.add(new File(this.source + fileName));
        }
        return files;
    }

    public void writeResultFile(FileExportContent fileExportContent, String orinigalFileName) throws ProcessFileException {
        createAndValidateDirectory(this.targetResult);
        String resultFileName = mountResultFileName(orinigalFileName);
        try (BufferedWriter buffer = new BufferedWriter(new FileWriter(resultFileName))) {
            buffer.append(fileExportContent.getCustomerAmountMessage());
            buffer.newLine();
            buffer.append(fileExportContent.getSellersAmountMessage());
            buffer.newLine();
            buffer.append(fileExportContent.getMoreExpansiveSaleMessage());
            buffer.newLine();
            buffer.append(fileExportContent.getWorstSellerMessage());
        } catch (Exception e) {
            throw new ProcessFileException("Erro ao escrever arquivo de resultados para o arquivo " + orinigalFileName);
        }
    }

    private String mountResultFileName(String originFileName) {
        DateTimeFormatter formatterData = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String nameFiltred = originFileName.replace(DEFAULT_FILE_EXTENSION, "");
        return this.targetResult + "Result_" + nameFiltred + formatterData.format(LocalDateTime.now()) + DEFAULT_FILE_EXTENSION;
    }

    public void moveProcessedFile(File originalFile) throws ProcessFileException {
        try {
            createAndValidateDirectory(this.targetDone);
            File destinyFile = new File(this.targetDone + originalFile.getName());
            Files.move(originalFile.toPath(), destinyFile.toPath());
        } catch (IOException e) {
            throw new ProcessFileException("Erro ao mover arquivo " + originalFile.getName() + " para o diretorio de processados.", e);
        }
    }

}

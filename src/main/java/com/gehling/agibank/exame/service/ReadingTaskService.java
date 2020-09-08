package com.gehling.agibank.exame.service;

import com.gehling.agibank.exame.dto.FileExportContent;
import com.gehling.agibank.exame.dto.FileImportedContent;
import com.gehling.agibank.exame.exception.ProcessFileException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;

@Slf4j
@Service
public class ReadingTaskService {

    @Autowired
    private FileManipulationService fileManipulationService;
    @Autowired
    private FileConvertService fileConvertService;
    @Autowired
    private FileProcessService fileProcessService;

    public void runProcess() throws ProcessFileException {
        log.debug("Start New Job!");
        ArrayList<File> filesToProcess = getFilesToProcess();
        filesToProcess.forEach(file -> {
            try {
                FileImportedContent fileImportedContent = this.fileConvertService.getFileContent(file);
                FileExportContent fileExportContent = this.fileProcessService.mountInfoToExport(fileImportedContent);
                this.fileManipulationService.writeResultFile(fileExportContent, file.getName());
                this.fileManipulationService.moveProcessedFile(file);
            } catch (ProcessFileException e) {
                log.error(e.getMessage(), e);
            }
        });
        log.debug("Job Completed!");
    }

    private ArrayList<File> getFilesToProcess() throws ProcessFileException {
        File directory = this.fileManipulationService.identifyDirectory();
        return this.fileManipulationService.findValidFiles(directory.list());
    }

}

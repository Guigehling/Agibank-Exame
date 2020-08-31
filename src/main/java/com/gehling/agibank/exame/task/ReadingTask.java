package com.gehling.agibank.exame.task;

import com.gehling.agibank.exame.service.ReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ReadingTask {

    @Autowired
    private ReadingService readingService;

    @Scheduled(cron = "${scheduler.cron.reading}")
    private void execute() throws IOException {
        this.readingService.readNewImportedFile();
    }
}

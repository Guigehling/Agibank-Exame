package com.gehling.agibank.exame.task;

import com.gehling.agibank.exame.exception.ProcessFileException;
import com.gehling.agibank.exame.service.ReadingTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReadingTask {

    @Autowired
    private ReadingTaskService readingTaskService;

    @Scheduled(cron = "${scheduler.cron.reading}")
    private void execute() throws ProcessFileException {
        this.readingTaskService.runProcess();
    }
}

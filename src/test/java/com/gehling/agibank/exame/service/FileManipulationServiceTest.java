package com.gehling.agibank.exame.service;

import com.gehling.agibank.exame.mocks.FileManipulationServiceMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class FileManipulationServiceTest {

    @InjectMocks
    private FileManipulationService fileManipulationService;

    @Test
    public void mustReturnListOfAllItems() {
        //GIVEN
        String[] directoryFiles = FileManipulationServiceMock.getAllFilesMatchsList();

        //THEN
        assertEquals(directoryFiles.length, fileManipulationService.findValidFiles(directoryFiles).size());
    }

    @Test
    public void mustReturnListTheOnlyCorrectItem() {
        //GIVEN
        String[] directoryFiles = FileManipulationServiceMock.getOneFilesMatchsList();

        //THEN
        assertEquals(1, fileManipulationService.findValidFiles(directoryFiles).size());
    }

    @Test
    public void mustReturnAnEmptyListWhenNoMatchOccurs() {
        //GIVEN
        String[] directoryFiles = FileManipulationServiceMock.getNoneFilesMatchsList();

        //THEN
        assertEquals(0, fileManipulationService.findValidFiles(directoryFiles).size());
    }

}

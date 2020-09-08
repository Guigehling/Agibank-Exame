package com.gehling.agibank.exame.service;

import com.gehling.agibank.exame.dto.FileImportedContent;
import com.gehling.agibank.exame.mocks.FileProcessServiceMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class FileProcessServiceTest {

    @InjectMocks
    private FileProcessService fileProcessService;

    @Test
    public void mustReturnSpecifiedSellerWithHighestBalance() {
        //GIVEN
        FileImportedContent fileImportedContent = FileProcessServiceMock.getListWithOneWrostSeller();

        //THEN
        assertEquals("Vendedor_B", fileProcessService.identifyTheWorstSeller(fileImportedContent).getName());
    }

}

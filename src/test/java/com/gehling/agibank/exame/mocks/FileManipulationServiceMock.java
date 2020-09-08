package com.gehling.agibank.exame.mocks;

public class FileManipulationServiceMock {

    public static String[] getAllFilesMatchsList() {
        String[] directoryFiles = new String[3];
        directoryFiles[0] = "file_01.txt";
        directoryFiles[1] = "file_02.txt";
        directoryFiles[2] = "last_file.txt";
        return directoryFiles;
    }

    public static String[] getOneFilesMatchsList() {
        String[] directoryFiles = new String[3];
        directoryFiles[0] = "data.txt";
        directoryFiles[1] = "whatever.jsp";
        directoryFiles[2] = "file_03.csv";
        return directoryFiles;
    }

    public static String[] getNoneFilesMatchsList() {
        String[] directoryFiles = new String[3];
        directoryFiles[0] = "data.pdf";
        directoryFiles[1] = "whatever.jsp";
        directoryFiles[2] = "file_03.csv";
        return directoryFiles;
    }

}

package com.gehling.agibank.exame.exception;

public class ProcessFileException extends Exception {

    public ProcessFileException(String reason) {
        super(reason);
    }

    public ProcessFileException(String reason, Throwable cause) {
        super(reason, cause);
    }

}

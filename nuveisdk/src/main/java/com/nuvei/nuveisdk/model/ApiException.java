package com.nuvei.nuveisdk.model;

import java.io.IOException;

public class ApiException extends IOException {
    private final String errorType;
    private final String helpMessage;

    public ApiException(String errorType, String helpMessage){
        super(helpMessage);
        this.errorType = errorType;
        this.helpMessage = helpMessage;
    }

    public String getErrorType() {
        return errorType;
    }

    public String getHelpMessage() {
        return helpMessage;
    }

}

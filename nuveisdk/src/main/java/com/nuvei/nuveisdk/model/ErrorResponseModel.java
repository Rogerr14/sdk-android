package com.nuvei.nuveisdk.model;

public class ErrorResponseModel {
    private ErrorData error;

    public ErrorResponseModel(ErrorData error) {
        this.error = error;
    }

    public ErrorData getError() {
        return error;
    }

    public void setError(ErrorData error) {
        this.error = error;
    }
}

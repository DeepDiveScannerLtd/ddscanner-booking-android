package com.ddscanner.booking.models.errors;

public class BadRequestException extends Exception {
    private GeneralError generalError;

    public GeneralError getGeneralError() {
        return generalError;
    }

    public BadRequestException setGeneralError(GeneralError generalError) {
        this.generalError = generalError;
        return this;
    }
}

package com.ddscanner.booking.models.errors;

public class UnknownErrorException extends Exception {
    private GeneralError generalError;

    public GeneralError getGeneralError() {
        return generalError;
    }

    public UnknownErrorException setGeneralError(GeneralError generalError) {
        this.generalError = generalError;
        return this;
    }
}

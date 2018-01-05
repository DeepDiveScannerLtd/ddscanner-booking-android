package com.ddscanner.booking.models.errors;

public class DiveSpotNotFoundException extends Exception {
    private GeneralError generalError;

    public GeneralError getGeneralError() {
        return generalError;
    }

    public DiveSpotNotFoundException setGeneralError(GeneralError generalError) {
        this.generalError = generalError;
        return this;
    }
}

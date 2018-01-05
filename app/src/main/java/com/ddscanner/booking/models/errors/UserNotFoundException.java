package com.ddscanner.booking.models.errors;

public class UserNotFoundException extends Exception {
    private GeneralError generalError;

    public GeneralError getGeneralError() {
        return generalError;
    }

    public UserNotFoundException setGeneralError(GeneralError generalError) {
        this.generalError = generalError;
        return this;
    }
}

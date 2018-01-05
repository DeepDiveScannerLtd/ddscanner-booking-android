package com.ddscanner.booking.models.errors;

public class CommentNotFoundException extends Exception {
    private GeneralError generalError;

    public GeneralError getGeneralError() {
        return generalError;
    }

    public CommentNotFoundException setGeneralError(GeneralError generalError) {
        this.generalError = generalError;
        return this;
    }
}

package com.ensd.database;

public class ModelValidateException extends Throwable {
    private final String validateFailedField;

    public ModelValidateException(String validateFailedField) {
        super();
        this.validateFailedField = validateFailedField;
    }

    public String getErrorCode() {
        return validateFailedField;
    }
}

package com.ensd.database;

public class ModelTypeException extends Throwable {
    private final String typeError;


    public ModelTypeException(String typeError) {
        super();
        this.typeError = typeError;
    }

    public String getErrorCode() {
        return typeError;
    }
}

package com.github.gasfgrv.storage.exception;

public class FileNotFoundException extends RuntimeException {

    public FileNotFoundException() {
        super("This file does not exist in the database");
    }

}

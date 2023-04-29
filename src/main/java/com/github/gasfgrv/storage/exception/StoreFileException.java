package com.github.gasfgrv.storage.exception;


import lombok.Getter;

@Getter
public class StoreFileException extends RuntimeException {

    private final String fileName;

    public StoreFileException(String fileName, Throwable cause) {
        super("Error creating file: %s".formatted(fileName), cause);
        this.fileName = fileName;
    }

}

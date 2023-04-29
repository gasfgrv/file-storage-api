package com.github.gasfgrv.storage.exception;

import lombok.Getter;

@Getter
public class UploadFileException extends RuntimeException {

    private final String fileName;

    public UploadFileException(String fileName, Throwable cause) {
        super("Could not upload the file: %s".formatted(fileName), cause);
        this.fileName = fileName;
    }

}

package com.github.gasfgrv.storage.utils;

import com.github.gasfgrv.storage.model.FileDB;
import java.util.Random;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class FileBuilder {

    private Random random;

    public FileBuilder() {
        this.random = new Random();
    }

    public FileDB newFile() {
        return new FileDB(
                UUID.fromString("fa8c627e-e1c1-4bf0-bb76-3494ef767413"),
                "teste.txt",
                MediaType.TEXT_PLAIN_VALUE,
                UUID.randomUUID().toString().getBytes()
        );
    }

    public MultipartFile newMultipartFile() {
        return new MockMultipartFile(
                "file",
                "teste.txt",
                MediaType.TEXT_PLAIN_VALUE,
                UUID.randomUUID().toString().getBytes()
        );
    }

}

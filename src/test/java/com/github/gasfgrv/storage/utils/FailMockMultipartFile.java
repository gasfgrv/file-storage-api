package com.github.gasfgrv.storage.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

public class FailMockMultipartFile implements MultipartFile {

    @Override
    public @NotNull String getName() {
        return "teste";
    }

    @Override
    public String getOriginalFilename() {
        return "teste.txt";
    }

    @Override
    public String getContentType() {
        return MediaType.TEXT_PLAIN_VALUE;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public long getSize() {
        return 0;
    }

    @Override
    public byte[] getBytes() throws IOException {
        throw new RuntimeException("Error getting file data");
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return null;
    }

    @Override
    public void transferTo(@NotNull File dest) throws IOException, IllegalStateException {
        throw new RuntimeException("Not implemented, only for testing");
    }
}

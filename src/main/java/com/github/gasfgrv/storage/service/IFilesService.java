package com.github.gasfgrv.storage.service;

import com.github.gasfgrv.storage.model.FileDB;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.web.multipart.MultipartFile;

public interface IFilesService {
    FileDB store(MultipartFile file);

    FileDB getFile(UUID id);

    Stream<FileDB> getAllFiles();

}

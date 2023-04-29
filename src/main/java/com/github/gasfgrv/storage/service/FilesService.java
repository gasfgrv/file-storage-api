package com.github.gasfgrv.storage.service;

import com.github.gasfgrv.storage.exception.FileNotFoundException;
import com.github.gasfgrv.storage.exception.StoreFileException;
import com.github.gasfgrv.storage.model.FileDB;
import com.github.gasfgrv.storage.repository.FilesRepository;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FilesService implements IFilesService {

    private final FilesRepository filesRepository;

    @Override
    public FileDB store(MultipartFile file) {
        try {
            var fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            var fileDB = new FileDB(UUID.randomUUID(), fileName, file.getContentType(), file.getBytes());
            return filesRepository.save(fileDB);
        } catch (IOException exception) {
            throw new StoreFileException(file.getOriginalFilename(), exception);
        }
    }

    @Override
    public FileDB getFile(UUID id) {
        return filesRepository.findById(id)
                .orElseThrow(FileNotFoundException::new);
    }

    @Override
    public Stream<FileDB> getAllFiles() {
        return filesRepository.findAll().stream();
    }

}

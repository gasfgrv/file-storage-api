package com.github.gasfgrv.storage.service;

import com.github.gasfgrv.storage.exception.FileNotFoundException;
import com.github.gasfgrv.storage.exception.StoreFileException;
import com.github.gasfgrv.storage.model.FileDB;
import com.github.gasfgrv.storage.repository.FilesRepository;
import com.github.gasfgrv.storage.utils.FailMockMultipartFile;
import com.github.gasfgrv.storage.utils.FileBuilder;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
class FilesServiceTest {

    @InjectMocks
    FilesService service;

    @Mock
    FilesRepository repository;

    private FileDB file;

    @BeforeEach
    void setUp() {
        file = new FileBuilder().newFile();
    }

    @Test
    void getAllFilesWithData() {
        var files = Collections.singletonList(file);

        given(repository.findAll()).willReturn(files);

        assertThat(service.getAllFiles())
                .isNotEmpty();

        verify(repository, times(1))
                .findAll();
    }

    @Test
    void getAllFilesWithNoData() {
        given(repository.findAll()).willReturn(Collections.emptyList());

        assertThat(service.getAllFiles())
                .isEmpty();

        verify(repository, times(1))
                .findAll();
    }

    @Test
    void getOneFileWithData() {
        given(repository.findById(any(UUID.class))).willReturn(Optional.of(file));

        assertThat(service.getFile(file.getId()))
                .isNotNull();

        verify(repository, times(1))
                .findById(any(UUID.class));
    }

    @Test
    void getOneFileWithNoData() {
        var id = file.getId();

        given(repository.findById(any(UUID.class))).willReturn(Optional.empty());

        assertThatExceptionOfType(FileNotFoundException.class)
                .isThrownBy(() -> service.getFile(id))
                .withMessage("This file does not exist in the database");

        verify(repository, times(1))
                .findById(any(UUID.class));
    }

    @Test
    void storeAFile() {
        var multipart = new FileBuilder().newMultipartFile();

        given(repository.save(any(FileDB.class))).willReturn(file);

        assertThatCode(() -> service.store(multipart))
                .doesNotThrowAnyException();

        verify(repository, times(1))
                .save(any(FileDB.class));
    }

    @Test
    void storeAFileWithThrowingException() {
        var multpart = new FailMockMultipartFile();

        assertThatExceptionOfType(StoreFileException.class)
                .isThrownBy(() -> service.store(multpart))
                .withMessageContaining("Error creating file")
                .withCauseInstanceOf(RuntimeException.class);

        verify(repository, never())
                .save(any(FileDB.class));
    }

}
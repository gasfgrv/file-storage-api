package com.github.gasfgrv.storage.repository;

import com.github.gasfgrv.storage.configuration.TestConfiguration;
import com.github.gasfgrv.storage.model.FileDB;
import com.github.gasfgrv.storage.utils.FileBuilder;
import java.util.Objects;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
class FilesRepositoryTest extends TestConfiguration {

    @Autowired
    FilesRepository repository;

    private FileDB file;

    @BeforeEach
    void setUp() {
        var fileDB = new FileBuilder().newFile();
        file = repository.save(fileDB);
    }

    @Test
    void mustStoreAFileInTheDatabase() {
        assertThat(repository.existsById(file.getId())).isTrue();
    }

    @Test
    void mustReturnAFileSavedInTheDatabase() {
        var fileInDB = repository.findById(file.getId());

        assertThat(repository.count()).isEqualTo(1);

        assertThat(fileInDB).isPresent();
        assertThat(fileInDB.get().getId())
                .isEqualTo(UUID.fromString("fa8c627e-e1c1-4bf0-bb76-3494ef767413"));
        assertThat(fileInDB.get().getName())
                .isEqualTo("teste.txt");
        assertThat(fileInDB.get().getType())
                .isEqualTo("text/plain");
        assertThat(fileInDB.get().getData())
                .hasSize(36);
    }

    @Test
    void mustReturnAllFilesSavedInTheDatabase() {
        var filesInDB = repository.findAll();

        assertThat(repository.count()).isEqualTo(1);

        assertThat(filesInDB)
                .isNotEmpty()
                .hasSize(1)
                .allMatch(Objects::nonNull);
    }
}
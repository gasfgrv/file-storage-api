package com.github.gasfgrv.storage.controller;

import com.github.gasfgrv.storage.exception.StoreFileException;
import com.github.gasfgrv.storage.exception.UploadFileException;
import com.github.gasfgrv.storage.model.FileDB;
import com.github.gasfgrv.storage.service.IFilesService;
import com.github.gasfgrv.storage.utils.FileBuilder;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
class FilesControlerTest {

    @InjectMocks
    FilesControler controler;

    @Mock
    IFilesService service;

    private FileDB file;

    private MultipartFile multpart;

    @BeforeEach
    void setUp() {
        file = new FileBuilder().newFile();
        multpart = new FileBuilder().newMultipartFile();
    }

    @Test
    void filesInfo() {
        var files = Stream.of(file);

        var request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        given(service.getAllFiles()).willReturn(files);

        var response = controler.filesInfo();

        var statusCode = response.getStatusCode();
        assertThat(statusCode.is2xxSuccessful()).isTrue();

        var body = response.getBody();
        assertThat(body)
                .isNotEmpty()
                .anyMatch(responseFile -> responseFile.getUrl().contains(file.getId().toString()));
    }

    @Test
    void getFileDetais() {
        given(service.getFile(file.getId())).willReturn(file);

        var response = controler.getFileDetais(file.getId());

        var statusCode = response.getStatusCode();
        assertThat(statusCode.is2xxSuccessful()).isTrue();

        var body = response.getBody();
        assertThat(body)
                .isNotNull()
                .matches(detail -> detail.getId().equals(file.getId()));
    }

    @Test
    void downloadFile() {
        given(service.getFile(file.getId())).willReturn(file);

        var response = controler.downloadFile(file.getId());

        var statusCode = response.getStatusCode();
        assertThat(statusCode.is2xxSuccessful()).isTrue();

        var body = response.getBody();
        assertThat(body)
                .isNotNull()
                .matches(bytes -> bytes.length == 36);
    }

    @Test
    void uploadFile() {
        given(service.store(multpart)).willReturn(file);

        var response = controler.uploadFile(multpart);

        var statusCode = response.getStatusCode();
        assertThat(statusCode.is2xxSuccessful()).isTrue();

        var body = response.getBody();
        assertThat(body)
                .isNotNull()
                .matches(message -> message.getMessage().contains("Uploaded the file successfully"));
    }

    @Test
    void uploadFileThrowingExeception() {
        var exception = new StoreFileException(file.getName(), new RuntimeException());

        given(service.store(multpart)).willThrow(exception);

        assertThatExceptionOfType(UploadFileException.class)
                .isThrownBy(() -> controler.uploadFile(multpart))
                .withMessageContaining("Could not upload the file")
                .withMessageContaining(file.getName())
                .withCauseInstanceOf(StoreFileException.class);
    }

}
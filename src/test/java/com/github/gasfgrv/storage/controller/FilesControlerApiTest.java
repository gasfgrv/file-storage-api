package com.github.gasfgrv.storage.controller;

import com.github.gasfgrv.storage.configuration.TestConfiguration;
import com.github.gasfgrv.storage.utils.FileBuilder;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Testcontainers
@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FilesControlerApiTest extends TestConfiguration {

    @Autowired
    WebApplicationContext webApplicationContext;

    MockMvc mockMvc;
    private MultipartFile multipart;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        multipart = new FileBuilder().newMultipartFile();
    }

    @Test
    void uploadFileMustReturn200() throws Exception {
        mockMvc.perform(multipart("/v1/files/upload")
                        .file((MockMultipartFile) multipart)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Uploaded the file successfully: teste.txt"))
                .andExpect(jsonPath("$._links").isMap());
    }

    @Test
    void filesInfoMustReturn200() throws Exception {
        mockMvc.perform(multipart("/v1/files/upload")
                .file((MockMultipartFile) multipart)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON));

        mockMvc.perform(get("/v1/files/info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getFileDetaisFileReturns200() throws Exception {
        var save = mockMvc.perform(multipart("/v1/files/upload")
                .file((MockMultipartFile) multipart)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON));

        var id = save.andReturn().getResponse()
                .getContentAsString().substring(107, 143);

        mockMvc.perform(get("/v1/files/{id}/details", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void downloadFileReturns200() throws Exception {
        var save = mockMvc.perform(multipart("/v1/files/upload")
                .file((MockMultipartFile) multipart)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON));

        var id = save.andReturn().getResponse()
                .getContentAsString().substring(107, 143);

        mockMvc.perform(get("/v1/files/{id}/download", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void getFileDetaisFileReturns404() throws Exception {
        mockMvc.perform(get("/v1/files/{id}/details", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("File not Found"));
    }

    @Test
    void uploadFileMustReturn417() throws Exception {
        var multpartFailed = new MockMultipartFile("file", UUID.randomUUID().toString().getBytes());

        mockMvc.perform(multipart("/v1/files/upload")
                        .file(multpartFailed)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isExpectationFailed())
                .andExpect(jsonPath("$.title").value("Error when uploading"));
    }

}
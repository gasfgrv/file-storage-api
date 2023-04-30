package com.github.gasfgrv.storage;

import com.github.gasfgrv.storage.controller.FilesControler;
import com.github.gasfgrv.storage.controller.FilesControllerAdvice;
import com.github.gasfgrv.storage.repository.FilesRepository;
import com.github.gasfgrv.storage.service.FilesService;
import org.junit.jupiter.api.Test;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FileStorageApiApplicationTests {

    @Test
    void contextLoads(ApplicationContext context) {
        assertThat(context.getBean(GroupedOpenApi.class)).isNotNull();
        assertThat(context.getBean(FilesControler.class)).isNotNull();
        assertThat(context.getBean(FilesControllerAdvice.class)).isNotNull();
        assertThat(context.getBean(FilesRepository.class)).isNotNull();
        assertThat(context.getBean(FilesService.class)).isNotNull();
    }

}

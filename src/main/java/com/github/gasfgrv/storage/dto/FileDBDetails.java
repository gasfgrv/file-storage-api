package com.github.gasfgrv.storage.dto;

import com.github.gasfgrv.storage.model.FileDB;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class FileDBDetails extends RepresentationModel<FileDBDetails> {
    private UUID id;
    private String name;
    private String type;

    public FileDBDetails(FileDB file) {
        this(file.getId(), file.getName(), file.getType());
    }
}

package com.github.gasfgrv.storage.controller;

import com.github.gasfgrv.storage.dto.FileDBDetails;
import com.github.gasfgrv.storage.dto.ResponseFile;
import com.github.gasfgrv.storage.dto.ResponseMessage;
import com.github.gasfgrv.storage.exception.UploadFileException;
import com.github.gasfgrv.storage.service.IFilesService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/v1/files")
@RequiredArgsConstructor
public class FilesControler {

    private final IFilesService filesService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            var storedFile = filesService.store(file);
            var message = new ResponseMessage("Uploaded the file successfully: %s".formatted(file.getOriginalFilename()))
                    .add(linkTo(methodOn(FilesControler.class).getFileDetais(storedFile.getId())).withSelfRel())
                    .add(linkTo(methodOn(FilesControler.class).downloadFile(storedFile.getId())).withRel("download"));
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            throw new UploadFileException(file.getOriginalFilename(), e);
        }
    }

    @GetMapping(value = "/{id}/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE )
    public ResponseEntity<byte[]> downloadFile(@PathVariable UUID id) {
        var file = filesService.getFile(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"%s\"".formatted(file.getName()))
                .body(file.getData());
    }

    @GetMapping(value = "/{id}/details", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FileDBDetails> getFileDetais(@PathVariable UUID id) {
        var details = new FileDBDetails(filesService.getFile(id))
                .add(linkTo(methodOn(FilesControler.class).filesInfo()).withRel("all"));

        return ResponseEntity.ok(details);
    }

    @GetMapping(value = "/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ResponseFile>> filesInfo() {
        var files = filesService.getAllFiles().map(fileDB -> {
            var uri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/files/")
                    .path(fileDB.getId().toString())
                    .toUriString();

            return new ResponseFile(fileDB.getName(), uri, fileDB.getType(), fileDB.getData().length)
                    .add(linkTo(methodOn(FilesControler.class).getFileDetais(fileDB.getId())).withSelfRel())
                    .add(linkTo(methodOn(FilesControler.class).downloadFile(fileDB.getId())).withRel("dowload"));
        }).toList();

        return ResponseEntity.ok().body(files);
    }

}

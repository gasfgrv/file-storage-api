package com.github.gasfgrv.storage.controller;

import com.github.gasfgrv.storage.exception.FileNotFoundException;
import com.github.gasfgrv.storage.exception.UploadFileException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class FilesControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(FileNotFoundException.class)
    @ApiResponse(responseCode = "404",
            content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                    schema = @Schema(implementation = Problem.class)))
    public ResponseEntity<Object> handleFileNotFoundException(FileNotFoundException exception, WebRequest request) {
        var status = HttpStatus.NOT_FOUND;
        var headers = getHeaders();
        var uri = getUri(request);

        var problema = Problem.create()
                .withType(URI.create("about:blank"))
                .withTitle("File not Found")
                .withDetail(exception.getMessage())
                .withInstance(uri)
                .withStatus(status);

        return handleExceptionInternal(exception, problema, headers, status, request);
    }

    @ExceptionHandler(UploadFileException.class)
    @ApiResponse(responseCode = "417",
            content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                    schema = @Schema(implementation = Problem.class)))
    public ResponseEntity<Object> handleUploadFileException(UploadFileException exception, WebRequest request) {
        var status = HttpStatus.EXPECTATION_FAILED;
        var headers = getHeaders();
        var uri = getUri(request);

        var properties = new HashMap<String, Object>();
        properties.put("fileName", exception.getFileName());
        properties.put("cause", exception.getCause().getMessage());

        var problema = Problem.create()
                .withType(URI.create("about:blank"))
                .withTitle("Error when uploading")
                .withDetail(exception.getMessage())
                .withInstance(uri)
                .withStatus(status)
                .withProperties(props -> props.putAll(properties));

        return handleExceptionInternal(exception, problema, headers, status, request);
    }

    @ExceptionHandler(SizeLimitExceededException.class)
    @ApiResponse(responseCode = "413",
            content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                    schema = @Schema(implementation = Problem.class)))
    public ResponseEntity<Object> handleSizeLimitExceededException(SizeLimitExceededException exception, WebRequest request) {
        var status = HttpStatus.PAYLOAD_TOO_LARGE;
        var headers = getHeaders();
        var uri = getUri(request);

        var problema = Problem.create()
                .withType(URI.create("about:blank"))
                .withTitle("Payload too large")
                .withDetail(exception.getMessage())
                .withInstance(uri)
                .withStatus(status);

        return handleExceptionInternal(exception, problema, headers, status, request);
    }

    private HttpHeaders getHeaders() {
        var headers = new HttpHeaders();
        headers.put(HttpHeaders.CONTENT_TYPE, Collections.singletonList(MediaType.APPLICATION_PROBLEM_JSON_VALUE));
        return headers;
    }

    private URI getUri(WebRequest request) {
        return URI.create(((ServletWebRequest) request).getRequest().getRequestURI());
    }
}

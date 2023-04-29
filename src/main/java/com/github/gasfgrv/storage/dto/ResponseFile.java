package com.github.gasfgrv.storage.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class ResponseFile extends RepresentationModel<ResponseFile> {
    private String name;
    private String url;
    private String type;
    private long size;
}

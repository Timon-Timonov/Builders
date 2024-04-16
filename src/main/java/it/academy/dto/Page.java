package it.academy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Page<DTO> {

    private final List<DTO> list;

    private final int pageNumber;
}

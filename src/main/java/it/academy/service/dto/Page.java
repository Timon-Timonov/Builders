package it.academy.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Page<DTO> {

    private final List<DTO> list;

    private final Integer pageNumber;

    private final Integer lastPageNumber;
}

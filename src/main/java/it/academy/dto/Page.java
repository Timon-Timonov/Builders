package it.academy.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor
public class Page<D> {

    private final List<D> list;

    private final Integer pageNumber;

    private final Integer lastPageNumber;

    private Map<D, Integer[]> map;
}

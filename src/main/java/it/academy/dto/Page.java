package it.academy.dto;

import it.academy.pojo.legalEntities.Developer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor
public class Page<DTO> {

    private final List<DTO> list;

    private final Integer pageNumber;

    private final Integer lastPageNumber;

    private Map<Developer, Integer> map;
}

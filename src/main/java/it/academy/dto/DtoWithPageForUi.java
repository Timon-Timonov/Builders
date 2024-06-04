package it.academy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class DtoWithPageForUi<T> {

    private Integer page;

    private Integer countOnPage;

    private Integer lastPageNumber;

    private Object status;

    private String exceptionMessage;

    private Long id;

    private String name;

    private List<T> list;

    private String url;

    private String search;
}

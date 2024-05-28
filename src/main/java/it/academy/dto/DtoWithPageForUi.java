package it.academy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class DtoWithPageForUi<T> {

    Integer page;

    Integer countOnPage;

    Integer lastPageNumber;

    Object status;

    String exceptionMessage;

    Long id;

    String name;

    List<T> list;

    String url;

    String search;
}

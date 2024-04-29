package it.academy.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static it.academy.util.constants.ServletURLs.LOGOUT_SERVLET;

@Builder
@Getter
@Setter
public class DtoWithPageForUi<T> {

    Integer page;
    Integer countOnPage;
    Object status;
    String exceptionMessage;

    Long id;
    String name;

    @Builder.Default
    List<T> list = new ArrayList<>();
    @Builder.Default
    String url = LOGOUT_SERVLET;


}

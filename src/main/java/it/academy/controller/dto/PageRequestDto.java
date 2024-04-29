package it.academy.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Setter
@Getter
@ToString
public class PageRequestDto {

    Long id;
    Long secondId;
    Object status;
    Integer page;
    Integer count;

    String name;
    String address;
}

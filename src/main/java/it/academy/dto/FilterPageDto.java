package it.academy.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Setter
@Getter
@ToString
public class FilterPageDto {

    Long id;
    Long secondId;
    Object status;
    Integer page;
    Integer count;

    String name;
    String address;
    String search;
}

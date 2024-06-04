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

    private Long id;

    private Long secondId;

    private Object status;

    private Integer page;

    private Integer count;

    private String name;

    private String address;

    private String search;
}

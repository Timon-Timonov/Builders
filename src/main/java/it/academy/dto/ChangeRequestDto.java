package it.academy.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class ChangeRequestDto {

    private Long id;

    private Object status;

    private String name;

    private int count;
}

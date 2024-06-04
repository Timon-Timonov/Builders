package it.academy.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class CreateRequestDto {

    private Long id;

    private String email;

    private String password;

    private String name;

    private String city;

    private String street;

    private String building;

    private Integer int1;

    private Integer int2;

    private Integer int3;

    private Long secondId;
}

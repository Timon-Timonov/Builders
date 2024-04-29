package it.academy.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class CreateRequestDto {

    Long id;
    String email;
    String password;
    String name;
    String city;
    String street;
    String building;
    Integer int1;
    Integer int2;
    Integer int3;
    Long secondId;
}

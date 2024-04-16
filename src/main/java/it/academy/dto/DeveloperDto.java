package it.academy.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeveloperDto {

    private Long id;

    private String developerName;

    private AddressDto developerAddress;

    private Integer developerDebt;
}

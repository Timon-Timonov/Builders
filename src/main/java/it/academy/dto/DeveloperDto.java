package it.academy.dto;

import lombok.*;

import static it.academy.util.constants.Constants.ZERO_INT_VALUE;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeveloperDto {

    private Long id;

    private String developerName;

    private String developerAddress;

    @Builder.Default
    private Integer developerDebt = ZERO_INT_VALUE;
}

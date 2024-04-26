package it.academy.dto;

import lombok.*;

import static it.academy.util.constants.Constants.ZERO_INT_VALUE;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractorDto {

    private Long id;

    private String contractorName;

    private AddressDto contractorAddress;

    @Builder.Default
    private Integer contractorDebt = ZERO_INT_VALUE;
}

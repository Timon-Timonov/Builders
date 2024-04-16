package it.academy.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractorDto {

    private Long id;

    private String contractorName;

    private AddressDto contractorAddress;

    private Integer contractorDebt;
}

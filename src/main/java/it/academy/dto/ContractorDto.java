package it.academy.dto;

import it.academy.pojo.Chapter;
import it.academy.pojo.Proposal;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

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

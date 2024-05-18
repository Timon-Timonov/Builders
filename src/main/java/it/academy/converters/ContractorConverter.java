package it.academy.converters;

import it.academy.dto.AddressDto;
import it.academy.dto.ContractorDto;
import it.academy.pojo.Address;
import it.academy.pojo.legalEntities.Contractor;

public class ContractorConverter {

    private ContractorConverter() {
    }

    public static ContractorDto convertToDto(Contractor contractor, Integer contractorDebt) {

        Address address = contractor.getAddress();
        if (address == null) {
            address = new Address();
        }
        return ContractorDto.builder()
                   .contractorAddress(AddressDto.builder()
                                          .city(address.getCity())
                                          .street(address.getStreet())
                                          .building(address.getBuilding())
                                          .build())
                   .id(contractor.getId())
                   .contractorName(contractor.getName())
                   .contractorDebt(contractorDebt)
                   .build();
    }
}

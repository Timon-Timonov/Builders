package it.academy.util.converters;

import it.academy.dto.AddressDto;
import it.academy.dto.ContractorDto;
import it.academy.dto.DeveloperDto;
import it.academy.pojo.Address;
import it.academy.pojo.legalEntities.Contractor;
import it.academy.pojo.legalEntities.Developer;

public class DeveloperConverter {

    private DeveloperConverter() {
    }

    public static DeveloperDto convertToDto(Developer developer, Integer developerDebt) {

        Address address = developer.getAddress();
        if (address == null) {
            address = new Address();
        }
        return DeveloperDto.builder()
                   .developerAddress(AddressDto.builder()
                                          .city(address.getCity())
                                          .street(address.getStreet())
                                          .building(address.getBuilding())
                                          .build())
                   .id(developer.getId())
                   .developerName(developer.getName())
                   .developerDebt(developerDebt)
                   .build();
    }
}

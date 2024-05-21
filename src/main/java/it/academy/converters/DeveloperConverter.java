package it.academy.converters;

import it.academy.dto.AddressDto;
import it.academy.dto.DeveloperDto;
import it.academy.pojo.Address;
import it.academy.pojo.legalEntities.Developer;
import it.academy.pojo.legalEntities.LegalEntity;

public class DeveloperConverter {

    private DeveloperConverter() {
    }

    public static DeveloperDto convertToDto(LegalEntity developer, Integer developerDebt) {

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

package it.academy.converters;

import it.academy.dto.DeveloperDto;
import it.academy.pojo.Address;
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
                   .developerAddress(address.toString())
                   .id(developer.getId())
                   .developerName(developer.getName())
                   .developerDebt(developerDebt)
                   .build();
    }
}

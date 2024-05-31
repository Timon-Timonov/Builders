package it.academy.converters;

import it.academy.dto.DeveloperDto;
import it.academy.pojo.Address;
import it.academy.pojo.legalEntities.LegalEntity;

import java.util.Optional;

import static it.academy.util.constants.Constants.ZERO_INT_VALUE;

public final class DeveloperConverter {

    private DeveloperConverter() {
    }

    public static DeveloperDto convertToDto(LegalEntity from, Integer[] values) {

        if (from == null) {
            return null;
        }
        Address address = Optional.ofNullable(from.getAddress()).orElse(new Address());
        int debt;
        if (values != null) {
            int workPrice = Optional.ofNullable(values[0]).orElse(ZERO_INT_VALUE);
            int transferSum = Optional.ofNullable(values[1]).orElse(ZERO_INT_VALUE);
            debt = workPrice - transferSum;
        } else {
            debt = ZERO_INT_VALUE;
        }
        return DeveloperDto.builder()
                   .developerAddress(address.toString())
                   .id(from.getId())
                   .developerName(from.getName())
                   .developerDebt(debt)
                   .build();
    }
}

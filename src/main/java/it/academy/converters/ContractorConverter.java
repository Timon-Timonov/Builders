package it.academy.converters;

import it.academy.dto.ContractorDto;
import it.academy.pojo.Address;
import it.academy.pojo.legalEntities.Contractor;

import java.util.Optional;

import static it.academy.util.constants.Constants.ZERO_INT_VALUE;

public final class ContractorConverter {

    private ContractorConverter() {
    }

    public static ContractorDto convertToDto(Contractor from, Integer[] values) {

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

        return ContractorDto.builder()
                   .contractorAddress(address.toString())
                   .id(from.getId())
                   .contractorName(from.getName())
                   .contractorDebt(debt)
                   .build();
    }
}

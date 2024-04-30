package it.academy.util.converters;

import it.academy.dto.CalculationDto;
import it.academy.dto.MoneyTransferDto;
import it.academy.pojo.MoneyTransfer;

public class MoneyTransferConverter {

    private MoneyTransferConverter() {
    }

    public static MoneyTransferDto convertToDto(MoneyTransfer from) {

        CalculationDto calculationDto = CalculationConverter.convertToDto(from.getCalculation(), null, null, null);
        return MoneyTransferDto.builder()
                   .id(from.getId())
                   .calculationDate(from.getCalculationDate())
                   .paymentType(from.getType())
                   .sum(from.getSum())
                   .calculationDto(calculationDto)
                   .build();
    }
}

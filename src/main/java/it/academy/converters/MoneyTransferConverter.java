package it.academy.converters;

import it.academy.dto.MoneyTransferDto;
import it.academy.pojo.MoneyTransfer;

public final class MoneyTransferConverter {

    private MoneyTransferConverter() {
    }

    public static MoneyTransferDto convertToDto(MoneyTransfer from) {

        return MoneyTransferDto.builder()
                   .id(from.getId())
                   .calculationDate(from.getCalculationDate())
                   .paymentType(from.getType())
                   .sum(from.getSum())
                   .build();
    }
}

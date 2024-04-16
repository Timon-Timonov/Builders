package it.academy.dto;

import it.academy.pojo.enums.PaymentType;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MoneyTransferDto {

    private Long id;

    private Integer sum;

    private Timestamp calculationDate;

    private PaymentType paymentType;

    private CalculationDto calculationDto;
}

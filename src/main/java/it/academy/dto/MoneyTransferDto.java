package it.academy.dto;

import it.academy.pojo.Calculation;
import it.academy.pojo.enums.PaymentType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
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

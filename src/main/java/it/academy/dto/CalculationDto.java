package it.academy.dto;

import it.academy.pojo.MoneyTransfer;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalculationDto {

    private Long id;

    private Integer YYYY;

    private Integer MM;

    private Integer workPricePlan;

    private Integer workPriceFact;

    private Integer sumForWork;

    private Integer sumAdvance;

    private Integer calculationDebt;

    private String chapterName;
}

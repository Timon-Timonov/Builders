package it.academy.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalculationDto {

    private Long id;

    private Integer yyyy;

    private Integer mm;

    private Integer workPricePlan;

    private Integer workPriceFact;

    private Integer sumForWork;

    private Integer sumAdvance;

    private Integer calculationDebt;

    private String chapterName;
}

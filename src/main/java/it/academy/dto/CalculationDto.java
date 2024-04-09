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

    private Integer sumSForWork;

    private Integer sumSAdvance;

    private Integer chapterDebt;

    private String chapterName;

    @Builder.Default
    private Set<MoneyTransfer> transferSet = new HashSet<>();
}

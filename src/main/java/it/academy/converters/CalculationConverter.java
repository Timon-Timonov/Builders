package it.academy.converters;

import it.academy.dto.CalculationDto;
import it.academy.pojo.Calculation;
import it.academy.pojo.Chapter;

import java.util.Optional;

import static it.academy.util.constants.Constants.*;

public final class CalculationConverter {

    private CalculationConverter() {
    }

    public static CalculationDto convertToDto(Calculation from, Integer[] values) {

        if (from == null) {
            return null;
        }
        Chapter chapter = Optional.ofNullable(from.getChapter()).orElse(new Chapter());
        int yyyy = Optional.of(from.getMonth().getYear()).orElse(-YEAR) + YEAR;
        int mm = Optional.of(from.getMonth().getMonth()).orElse(-MONTH) + MONTH;
        int debt;
        int workPriceFact;
        int sumAdvance;
        int sumForWork;
        if (values != null) {
            workPriceFact = Optional.ofNullable(from.getWorkPriceFact()).orElse(ZERO_INT_VALUE);
            sumAdvance = Optional.ofNullable(values[0]).orElse(ZERO_INT_VALUE);
            sumForWork = Optional.ofNullable(values[1]).orElse(ZERO_INT_VALUE);
            debt = workPriceFact - sumAdvance - sumForWork;
        } else {
            debt = ZERO_INT_VALUE;
            workPriceFact = ZERO_INT_VALUE;
            sumAdvance = ZERO_INT_VALUE;
            sumForWork = ZERO_INT_VALUE;
        }

        return CalculationDto.builder()
                   .id(from.getId())
                   .workPriceFact(workPriceFact)
                   .workPricePlan(from.getWorkPricePlan())
                   .chapterName(chapter.getName())
                   .yyyy(yyyy)
                   .mm(mm)
                   .sumAdvance(sumAdvance)
                   .sumForWork(sumForWork)
                   .calculationDebt(debt)
                   .build();
    }
}

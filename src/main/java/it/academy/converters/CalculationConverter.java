package it.academy.converters;

import it.academy.dto.CalculationDto;
import it.academy.pojo.Calculation;
import it.academy.pojo.Chapter;

import java.util.Optional;

import static it.academy.util.constants.Constants.*;

public class CalculationConverter {


    private CalculationConverter() {
    }

    public static CalculationDto convertToDto(Calculation from, Integer sumAdvance, Integer sumForWork) {

        Chapter chapter = from.getChapter();
        if (chapter == null) {
            chapter = new Chapter();
        }
        int YYYY = from.getMonth().getYear() + YEAR;
        int MM = from.getMonth().getMonth() + MONTH;
        int workPriceFact = Optional.ofNullable(from.getWorkPriceFact()).orElse(ZERO_INT_VALUE);
        int calculationDebt = workPriceFact
                                  - Optional.ofNullable(sumAdvance).orElse(ZERO_INT_VALUE)
                                  - Optional.ofNullable(sumForWork).orElse(ZERO_INT_VALUE);

        return CalculationDto.builder()

                   .id(from.getId())
                   .workPriceFact(workPriceFact)
                   .workPricePlan(from.getWorkPricePlan())
                   .chapterName(chapter.getName())
                   .YYYY(YYYY)
                   .MM(MM)
                   .sumAdvance(sumAdvance)
                   .sumForWork(sumForWork)
                   .calculationDebt(calculationDebt)
                   .build();
    }
}

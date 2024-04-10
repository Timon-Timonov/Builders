package it.academy.util.converters;

import it.academy.dto.CalculationDto;
import it.academy.pojo.Calculation;
import it.academy.pojo.Chapter;

public class CalculationConverter {

    private CalculationConverter() {
    }

    public static CalculationDto convertToDto(Calculation from, Integer calculationDebt, Integer sumAdvance, Integer sumForWork) {

        Chapter chapter = from.getChapter();
        if (chapter == null) {
            chapter = new Chapter();
        }
        int YYYY = from.getMonth().getYear() + 1900;
        int MM = from.getMonth().getMonth() + 1;

        return CalculationDto.builder()

                   .id(from.getId())
                   .workPriceFact(from.getWorkPriceFact())
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

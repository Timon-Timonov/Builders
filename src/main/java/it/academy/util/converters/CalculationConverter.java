package it.academy.util.converters;

import it.academy.dto.CalculationDto;
import it.academy.pojo.Calculation;
import it.academy.pojo.Chapter;

import static it.academy.util.Constants.MONTH;
import static it.academy.util.Constants.YEAR;

public class CalculationConverter {


    private CalculationConverter() {
    }

    public static CalculationDto convertToDto(Calculation from, Integer calculationDebt, Integer sumAdvance, Integer sumForWork) {

        Chapter chapter = from.getChapter();
        if (chapter == null) {
            chapter = new Chapter();
        }
        int YYYY = from.getMonth().getYear() + YEAR;
        int MM = from.getMonth().getMonth() + MONTH;

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

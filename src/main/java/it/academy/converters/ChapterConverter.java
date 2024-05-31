package it.academy.converters;

import it.academy.dto.ChapterDto;
import it.academy.dto.Page;
import it.academy.pojo.Address;
import it.academy.pojo.Chapter;
import it.academy.pojo.Project;
import it.academy.pojo.legalEntities.Contractor;
import it.academy.pojo.legalEntities.Developer;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static it.academy.util.constants.Constants.ZERO_INT_VALUE;

public final class ChapterConverter {

    private ChapterConverter() {
    }

    public static ChapterDto convertToDto(Chapter from, Integer[] values) {

        if (from == null) {
            return null;
        }
        Contractor contractor = Optional.ofNullable(from.getContractor()).orElse(new Contractor());
        Project project = Optional.ofNullable(from.getProject()).orElse(new Project());
        Address address = Optional.ofNullable(project.getAddress()).orElse(new Address());
        Developer developer = Optional.ofNullable(project.getDeveloper()).orElse(new Developer());
        int debt;
        if (values != null) {
            int workPrice = Optional.ofNullable(values[0]).orElse(ZERO_INT_VALUE);
            int transferSum = Optional.ofNullable(values[1]).orElse(ZERO_INT_VALUE);
            debt = workPrice - transferSum;
        } else {
            debt = ZERO_INT_VALUE;
        }

        return ChapterDto.builder()
                   .id(from.getId())
                   .chapterName(from.getName())
                   .chapterPrice(from.getPrice())
                   .chapterStatus(from.getStatus())
                   .contractorName(contractor.getName())
                   .developerName(developer.getName())
                   .projectAddress(address.toString())
                   .projectStatus(project.getStatus())
                   .projectName(project.getName())
                   .chapterDebt(debt)
                   .build();
    }

    public static void convertToDtoWithDebt(List<ChapterDto> list, Page<Chapter> page) {

        Map<Chapter, Integer[]> map = page.getMap();
        list.addAll(map.keySet().stream()
                        .map(chapter -> convertToDto(chapter, map.get(chapter)))
                        .collect(Collectors.toList()));
    }
}

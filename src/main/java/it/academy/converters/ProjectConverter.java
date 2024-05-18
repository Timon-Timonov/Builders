package it.academy.converters;

import it.academy.dto.AddressDto;
import it.academy.dto.ProjectDto;
import it.academy.pojo.Address;
import it.academy.pojo.Chapter;
import it.academy.pojo.Project;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.legalEntities.Developer;
import it.academy.util.Util;

import java.util.List;
import java.util.stream.Collectors;

import static it.academy.util.constants.Constants.ZERO_INT_VALUE;

public class ProjectConverter {

    private ProjectConverter() {
    }

    public static ProjectDto convertToDto(Project from, Integer projectPrice, Integer projectDebt) {

        Address address = from.getAddress();
        if (address == null) {
            address = new Address();
        }
        Developer developer = from.getDeveloper();
        if (developer == null) {
            developer = new Developer();
        }
        return ProjectDto.builder()
                   .id(from.getId())
                   .projectName(from.getName())
                   .status(from.getStatus())
                   .projectAddress(AddressDto.builder()
                                       .city(address.getCity())
                                       .street(address.getStreet())
                                       .building(address.getBuilding())
                                       .build())
                   .developerName(developer.getName())
                   .projectPrice(projectPrice)
                   .debtByProject(projectDebt)
                   .build();
    }

    public static ProjectDto getProjectDtoForContractor(long contractorId, Project project) {

        List<Chapter> list = project.getChapters().stream()
                                 .filter(chapter -> chapter.getContractor() != null)
                                 .filter(chapter -> chapter.getContractor().getId().equals(contractorId))
                                 .collect(Collectors.toList());

        Integer projectPrice = list.stream()
                                   .map(Chapter::getPrice)
                                   .reduce(ZERO_INT_VALUE, Integer::sum);
        Integer projectDebt = list.stream()
                                  .map(Util::getDebtByChapter)
                                  .reduce(ZERO_INT_VALUE, Integer::sum);
        return ProjectConverter.convertToDto(project, projectPrice, projectDebt);
    }
}

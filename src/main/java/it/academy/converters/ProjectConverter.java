package it.academy.converters;

import it.academy.dto.ProjectDto;
import it.academy.pojo.Address;
import it.academy.pojo.Project;
import it.academy.pojo.legalEntities.Developer;

public final class ProjectConverter {

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
                   .projectAddress(address.toString())
                   .developerName(developer.getName())
                   .projectPrice(projectPrice)
                   .debtByProject(projectDebt)
                   .build();
    }
}

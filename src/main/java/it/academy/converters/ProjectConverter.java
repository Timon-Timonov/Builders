package it.academy.converters;

import it.academy.dto.ProjectDto;
import it.academy.pojo.Address;
import it.academy.pojo.Project;
import it.academy.pojo.legalEntities.Developer;

import java.util.Optional;

import static it.academy.util.constants.Constants.ZERO_INT_VALUE;

public final class ProjectConverter {

    private ProjectConverter() {
    }

    public static ProjectDto convertToDto(Project from, Integer[] values) {

        if (from == null) {
            return null;
        }
        Address address = Optional.ofNullable(from.getAddress()).orElse(new Address());
        Developer developer = Optional.ofNullable(from.getDeveloper()).orElse(new Developer());
        int projectPrice;
        int workPrice;
        int transferSum;
        if (values != null) {
            projectPrice = Optional.ofNullable(values[0]).orElse(ZERO_INT_VALUE);
            workPrice = Optional.ofNullable(values[1]).orElse(ZERO_INT_VALUE);
            transferSum = Optional.ofNullable(values[2]).orElse(ZERO_INT_VALUE);
        } else {
            projectPrice = ZERO_INT_VALUE;
            workPrice = ZERO_INT_VALUE;
            transferSum = ZERO_INT_VALUE;
        }

        return ProjectDto.builder()
                   .id(from.getId())
                   .projectName(from.getName())
                   .status(from.getStatus())
                   .projectAddress(address.toString())
                   .developerName(developer.getName())
                   .projectPrice(projectPrice)
                   .debtByProject(workPrice - transferSum)
                   .build();
    }
}

package it.academy.util.converters;

import it.academy.dto.AddressDto;
import it.academy.dto.ProjectDto;
import it.academy.pojo.Address;
import it.academy.pojo.Project;
import it.academy.pojo.legalEntities.Developer;

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

    public static Project convertToEntity(ProjectDto from) {

        AddressDto addressFrom = from.getProjectAddress();
        if (addressFrom == null) {
            addressFrom = new AddressDto();
        }

        return Project.builder()
                   .address(Address.builder()
                                .city(addressFrom.getCity())
                                .street(addressFrom.getStreet())
                                .building(addressFrom.getBuilding())
                                .build())
                   .name(from.getProjectName())
                   .developer(Developer.builder()
                                  .id(from.getDeveloperId())
                                  .name(from.getDeveloperName())
                                  .build())
                   .build();
    }
}

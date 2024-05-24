package it.academy.converters;

import it.academy.dto.AddressDto;
import it.academy.dto.ChapterDto;
import it.academy.pojo.Address;
import it.academy.pojo.Chapter;
import it.academy.pojo.Project;
import it.academy.pojo.legalEntities.Contractor;
import it.academy.pojo.legalEntities.Developer;

public class ChapterConverter {

    private ChapterConverter() {
    }

    public static ChapterDto convertToDto(Chapter from, Integer chapterDebt) {

        Contractor contractor = from.getContractor();
        if (contractor == null) {
            contractor = new Contractor();
        }
        Project project = from.getProject();
        if (project == null) {
            project = new Project();
        }
        Address address = project.getAddress();
        if (address == null) {
            address = new Address();
        }
        Developer developer = project.getDeveloper();
        if (developer == null) {
            developer = new Developer();
        }
        return ChapterDto.builder()
                   .id(from.getId())
                   .chapterName(from.getName())
                   .chapterPrice(from.getPrice())
                   .chapterStatus(from.getStatus())
                   .contractorName(contractor.getName())
                   .DeveloperName(developer.getName())
                   .projectAddress(AddressDto.builder()
                                       .city(address.getCity())
                                       .street(address.getStreet())
                                       .building(address.getBuilding())
                                       .build())
                   .projectStatus(project.getStatus())
                   .projectName(project.getName())
                   .chapterDebt(chapterDebt)
                   .build();
    }
}

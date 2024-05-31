package it.academy.converters;

import it.academy.dto.ProposalDto;
import it.academy.pojo.Chapter;
import it.academy.pojo.Project;
import it.academy.pojo.Proposal;
import it.academy.pojo.legalEntities.Contractor;
import it.academy.pojo.legalEntities.Developer;

import java.util.Optional;

public final class ProposalConverter {

    private ProposalConverter() {
    }

    public static ProposalDto convertToDto(Proposal from) {

        if (from == null) {
            return null;
        }
        Contractor contractor = Optional.ofNullable(from.getContractor()).orElse(new Contractor());
        Chapter chapter = Optional.ofNullable(from.getChapter()).orElse(new Chapter());
        Project project = Optional.ofNullable(chapter.getProject()).orElse(new Project());
        Developer developer = Optional.ofNullable(project.getDeveloper()).orElse(new Developer());

        return ProposalDto.builder()
                   .id(from.getId())
                   .chapterName(chapter.getName())
                   .projectName(project.getName())
                   .chapterPrice(chapter.getPrice())
                   .createdDate(from.getCreatedDate())
                   .contractorName(contractor.getName())
                   .developerName(developer.getName())
                   .status(from.getStatus())
                   .chapterId(chapter.getId())
                   .contractorId(contractor.getId())
                   .projectAddress(project.getAddress().toString())
                   .projectStatus(project.getStatus())
                   .build();
    }
}

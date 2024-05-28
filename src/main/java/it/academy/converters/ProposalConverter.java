package it.academy.converters;

import it.academy.dto.ProposalDto;
import it.academy.pojo.Chapter;
import it.academy.pojo.Project;
import it.academy.pojo.Proposal;
import it.academy.pojo.legalEntities.Contractor;
import it.academy.pojo.legalEntities.Developer;

public final class ProposalConverter {

    private ProposalConverter() {
    }

    public static ProposalDto convertToDto(Proposal proposal) {

        Chapter chapter = proposal.getChapter();
        if (chapter == null) {
            chapter = new Chapter();
        }
        Project project = chapter.getProject();
        if (project == null) {
            project = new Project();
        }
        Contractor contractor = proposal.getContractor();
        if (contractor == null) {
            contractor = new Contractor();
        }
        Developer developer = project.getDeveloper();
        if (developer == null) {
            developer = new Developer();
        }
        return ProposalDto.builder()
                   .id(proposal.getId())
                   .chapterName(chapter.getName())
                   .projectName(project.getName())
                   .chapterPrice(chapter.getPrice())
                   .createdDate(proposal.getCreatedDate())
                   .contractorName(contractor.getName())
                   .developerName(developer.getName())
                   .status(proposal.getStatus())
                   .chapterId(chapter.getId())
                   .contractorId(contractor.getId())
                   .projectAddress(project.getAddress().toString())
                   .projectStatus(project.getStatus())
                   .build();
    }
}

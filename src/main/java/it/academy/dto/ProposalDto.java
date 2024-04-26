package it.academy.dto;

import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.ProposalStatus;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProposalDto {

    private Long id;

    private long contractorId;

    private long chapterId;

    @Builder.Default
    private ProposalStatus status = ProposalStatus.CONSIDERATION;

    private String ProjectName;

    private String chapterName;

    private Integer chapterPrice;

    private String developerName;

    private String contractorName;

    private String projectAddress;

    private ProjectStatus projectStatus;

    private Timestamp createdDate;
}

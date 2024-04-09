package it.academy.dto;

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

    @Builder.Default
    private ProposalStatus status = ProposalStatus.CONSIDERATION;

    private String ProjectName;
    private String chapterName;

    private Integer chapterPrice;

    private String developerName;

    private String contractorName;

    private Timestamp createdDate;
}

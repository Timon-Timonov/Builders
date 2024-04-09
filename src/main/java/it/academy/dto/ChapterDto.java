package it.academy.dto;

import it.academy.pojo.enums.ChapterStatus;
import it.academy.pojo.enums.ProjectStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChapterDto {

    private Long id;

    private String chapterName;

    private Integer chapterPrice;

    @Builder.Default
    private ChapterStatus chapterStatus = ChapterStatus.FREE;
    private ProjectStatus projectStatus;

    private String projectName;

    private AddressDto projectAddress;

    private Integer chapterDebt;

    private String contractorName;

    private String DeveloperName;
}


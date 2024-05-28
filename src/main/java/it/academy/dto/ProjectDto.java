package it.academy.dto;

import it.academy.pojo.enums.ProjectStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDto {

    private Long id;

    private Long developerId;

    private String projectName;

    private String developerName;

    private String projectAddress;

    private Integer debtByProject;

    private Integer projectPrice;

    @Builder.Default
    private ProjectStatus status = ProjectStatus.PREPARATION;
}

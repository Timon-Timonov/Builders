package it.academy.dto;

import it.academy.pojo.Address;
import it.academy.pojo.Chapter;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.legalEntities.Developer;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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

    private AddressDto projectAddress;

    private Integer debtByProject;

    private Integer projectPrice;

    @Builder.Default
    private ProjectStatus status = ProjectStatus.PREPARATION;




}

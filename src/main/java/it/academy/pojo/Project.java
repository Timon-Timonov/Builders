package it.academy.pojo;

import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.legalEntities.Developer;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "projects")
public class Project implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotBlank
    private String name;

    @Column
    @Builder.Default
    private ProjectStatus status = ProjectStatus.PREPARATION;

    @ManyToOne
    @JoinColumn(name = "developer_id")
    @NotNull
    private Developer developer;

    @Builder.Default
    @Embedded
    @NotNull
    private Address address = new Address();
}

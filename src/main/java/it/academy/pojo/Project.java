package it.academy.pojo;

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
@EqualsAndHashCode(exclude = "chapters")
@ToString(exclude = "chapters")
@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    @Builder.Default
    private ProjectStatus status = ProjectStatus.PREPARATION;

    @ManyToOne
    @JoinColumn(name = "developer_id")
    private Developer developer;

    @Builder.Default
    @Embedded
    private Address address = new Address();

    @Builder.Default
    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private Set<Chapter> chapters = new HashSet<>();
}

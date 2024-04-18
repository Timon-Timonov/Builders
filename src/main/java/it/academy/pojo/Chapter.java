package it.academy.pojo;

import it.academy.pojo.enums.ChapterStatus;
import it.academy.pojo.enums.ProposalStatus;
import it.academy.pojo.legalEntities.Contractor;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"proposalSet", "calculationSet"})
@ToString(exclude = {"proposalSet", "calculationSet"})
@Entity
@Table(name = "chapters")
public class Chapter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private int price;

    @Column
    @Builder.Default
    private ChapterStatus status = ChapterStatus.FREE;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "contractor_id")
    private Contractor contractor;

    @Builder.Default
    @OneToMany(mappedBy = "chapter")
    private Set<Proposal> proposalSet = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "chapter", fetch = FetchType.EAGER)
    private Set<Calculation> calculationSet = new HashSet<>();
}

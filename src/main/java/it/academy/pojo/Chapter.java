package it.academy.pojo;

import it.academy.pojo.enums.ChapterStatus;
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
@EqualsAndHashCode(exclude = "proposalSet")
@ToString(exclude = "proposalSet")
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
    private ChapterStatus status = ChapterStatus.AKTIVE;

    @ManyToOne
    private BuildingObject buildingObject;

    @ManyToOne
    private Contractor contractor;

    @Builder.Default
    @OneToMany(mappedBy = "chapter")
    private Set<Proposal> proposalSet = new HashSet<>();

    @Builder.Default
    @OneToMany
    private Set<Calculation> calculationSet = new HashSet<>();

    public void addCalculation(Calculation calculation) {

        if (calculation != null) {
            calculation.setChapter(this);
            calculationSet.add(calculation);
        }
    }

    public void addProposal(Proposal proposal) {

        if (proposal != null) {
            proposal.setChapter(this);
            proposalSet.add(proposal);
        }
    }
}

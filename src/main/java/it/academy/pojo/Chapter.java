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
    private Project project;

    @ManyToOne
    private Contractor contractor;

    @Builder.Default
    @OneToMany(mappedBy = "chapter")
    private Set<Proposal> proposalSet = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "chapter", fetch = FetchType.EAGER)
    private Set<Calculation> calculationSet = new HashSet<>();

    public void setContractor(Contractor contractor) {

        if (ChapterStatus.FREE.equals(status) && contractor != null) {
            this.status = ChapterStatus.OCCUPIED;
            this.contractor = contractor;
            this.contractor.getChapters().add(this);
        }
    }

    public void setStatus(ChapterStatus newStatus) {

        if (ChapterStatus.CANCELED.equals(newStatus)) {
            proposalSet.forEach(proposal -> proposal.setStatus(ProposalStatus.CANCELED));
        }
        this.status = newStatus;
    }

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

package it.academy.pojo;


import it.academy.pojo.enums.ProposalStatus;
import it.academy.pojo.legalEntities.Contractor;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "proposals")
public class Proposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Builder.Default
    @Column
    private ProposalStatus status = ProposalStatus.CONSIDERATION;

    @ManyToOne
    private Chapter chapter;

    @ManyToOne
    private Contractor contractor;

    @Column(name = "created_date", updatable = false)
    @CreationTimestamp
    private Timestamp createdDate;

    public void setStatus(ProposalStatus status) {

        if (ProposalStatus.APPROVED.equals(this.status)
                && ProposalStatus.ACCEPTED_BY_CONTRACTOR.equals(status)) {
            this.chapter.setContractor(this.contractor);
        }
        this.status = status;
    }
}

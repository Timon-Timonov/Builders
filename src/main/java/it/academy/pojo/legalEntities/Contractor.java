package it.academy.pojo.legalEntities;

import it.academy.pojo.Chapter;
import it.academy.pojo.Proposal;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"chapters", "proposalSet"})
@ToString(callSuper = true, exclude = {"chapters", "proposalSet"})
@SuperBuilder
@Entity
@DiscriminatorValue("C")
public class Contractor extends LegalEntity {

    @Builder.Default
    @OneToMany(mappedBy = "contractor")
    private Set<Chapter> chapters = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "contractor")
    private Set<Proposal> proposalSet = new HashSet<>();
}


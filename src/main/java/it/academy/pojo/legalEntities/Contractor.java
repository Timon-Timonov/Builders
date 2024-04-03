package it.academy.pojo.legalEntities;

import it.academy.pojo.Chapter;
import it.academy.pojo.Proposal;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"chapters","proposalSet"})
@ToString(callSuper = true, exclude =  {"chapters","proposalSet"})
@SuperBuilder
@Entity
//@Table(name = "contractors")
@DiscriminatorValue("C")
public class Contractor extends LegalEntity {

    @Builder.Default
    @OneToMany
    private Set<Chapter> chapters = new HashSet<>();

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Proposal> proposalSet=new HashSet<>();
}


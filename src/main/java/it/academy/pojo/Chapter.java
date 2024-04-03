package it.academy.pojo;

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

    @ManyToOne
    private BuildingObject object;

    @ManyToOne
    private Contractor contractor;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Proposal> proposalSet=new HashSet<>();

    @Builder.Default
    @OneToMany
    private Set<Calculation> calculationSet=new HashSet<>();
}

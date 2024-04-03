package it.academy.pojo.legalEntities;

import it.academy.pojo.Address;
import it.academy.pojo.User;
import it.academy.pojo.enums.Roles;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@SuperBuilder
@Entity
@Table(name = "legal_entity")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "entity_type", discriminatorType = DiscriminatorType.CHAR)
@DiscriminatorValue("E")
public class LegalEntity {


    @GenericGenerator(name = "one-one",
        strategy = "foreign",
        parameters = @org.hibernate.annotations.Parameter(name = "property",
            value = "user"))
    @GeneratedValue(generator = "one-one")
    @Id
    @Column(name = "user_id")
    private Long id;

    @Column
    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private User user;

    @Embedded
    private Address address;
}

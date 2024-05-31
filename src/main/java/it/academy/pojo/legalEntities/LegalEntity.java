package it.academy.pojo.legalEntities;

import it.academy.pojo.Address;
import it.academy.pojo.User;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "user")
@ToString(exclude = "user")
@SuperBuilder
@Entity
@Table(name = "legal_entities")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "entity_type", discriminatorType = DiscriminatorType.CHAR)
@DiscriminatorValue("E")
public class LegalEntity implements Serializable {

    @GenericGenerator(name = "one-to-one",
        strategy = "foreign",
        parameters = @org.hibernate.annotations.Parameter(name = "property", value = "user"))
    @GeneratedValue(generator = "one-to-one")
    @Id
    private Long id;

    @Column
    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private User user;

    @Embedded
    private Address address;
}

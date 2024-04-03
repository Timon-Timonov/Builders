package it.academy.pojo.legalEntities;


import it.academy.pojo.BuildingObject;
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
@EqualsAndHashCode(callSuper = true, exclude = "buildingObjects")
@ToString(callSuper = true, exclude = "buildingObjects")
@SuperBuilder
@Entity
@DiscriminatorValue("D")
public class Developer extends LegalEntity {

    @Builder.Default
    @OneToMany(mappedBy = "developer")
    private Set<BuildingObject> buildingObjects = new HashSet<>();
}

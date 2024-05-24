package it.academy.pojo.legalEntities;


import it.academy.pojo.Project;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = "projects")
@ToString(callSuper = true, exclude = "projects")
@SuperBuilder
@Entity
@DiscriminatorValue("D")
public class Developer extends LegalEntity {

    @Builder.Default
    @OneToMany(mappedBy = "developer",fetch = FetchType.LAZY)
    private Set<Project> projects = new HashSet<>();
}

package it.academy.pojo.legalEntities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Entity
@DiscriminatorValue("D")
public class Developer extends LegalEntity {

}

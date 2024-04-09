package it.academy.dto;

import it.academy.pojo.Project;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeveloperDto {

    private Long id;

    private String developerName;

    private AddressDto developerAddress;

    private Integer developerDebt;

}

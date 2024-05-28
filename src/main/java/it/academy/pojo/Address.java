package it.academy.pojo;

import lombok.*;

import javax.persistence.Embeddable;

import static it.academy.util.constants.Constants.COMMA;
import static it.academy.util.constants.Constants.DOT;


@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class Address {

    private String city;

    private String street;

    private String building;

    @Override
    public String toString() {

        return city + COMMA + street + COMMA + building + DOT;
    }
}

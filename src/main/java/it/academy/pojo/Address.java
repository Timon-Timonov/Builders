package it.academy.pojo;

import lombok.*;

import javax.persistence.Embeddable;


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
        return city + ", " + street + ", " + building + ".";
    }
}

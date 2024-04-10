package it.academy.pojo;

import lombok.*;

import javax.persistence.Embeddable;


@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class Address {

    private String city;

    private String street;

    private String building;
}

package it.academy.pojo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@Embeddable
public class Address {
    
    private String City;
    
    private String street;
    
    private String building;
}

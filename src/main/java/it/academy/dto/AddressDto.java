package it.academy.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDto {

    private String city;

    private String street;

    private String building;

    @Override
    public String toString() {
        return city + ", " + street + ", " + building;
    }
}

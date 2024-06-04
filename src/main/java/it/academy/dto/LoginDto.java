package it.academy.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static it.academy.util.constants.ServletURLs.LOGOUT_SERVLET;

@Builder
@Getter
@Setter
public class LoginDto {

    private String exceptionMessage;

    @Builder.Default
    private UserDto userFromDb = new UserDto();

    @Builder.Default
    private String url = LOGOUT_SERVLET;
}

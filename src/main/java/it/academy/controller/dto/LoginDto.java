package it.academy.controller.dto;


import it.academy.dto.UserDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static it.academy.util.constants.ServletURLs.LOGOUT_SERVLET;

@Builder
@Getter
@Setter
public class LoginDto {

    String exceptionMessage;

    @Builder.Default
    UserDto userFromDb = new UserDto();

    @Builder.Default
    String url = LOGOUT_SERVLET;
}

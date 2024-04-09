package it.academy.dto;

import it.academy.pojo.enums.Roles;
import it.academy.pojo.enums.UserStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Long id;

    private String email;

    private String password;

    private UserStatus userStatus;

    private Roles userRole;
}

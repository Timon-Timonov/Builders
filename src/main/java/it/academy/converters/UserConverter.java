package it.academy.converters;

import it.academy.dto.UserDto;
import it.academy.pojo.User;

public class UserConverter {

    private UserConverter() {
    }

    public static UserDto convertToDto(User from) {

        return UserDto.builder()
                   .id(from.getId())
                   .email(from.getEmail())
                   .password(from.getPassword())
                   .userRole(from.getRole())
                   .userStatus(from.getStatus())
                   .build();
    }

    public static User convertToEntity(UserDto from) {

        return User.builder()
                   .id(from.getId())
                   .email(from.getEmail())
                   .role(from.getUserRole())
                   .password(from.getPassword())
                   .build();
    }

    public static UserDto createDto(String email, String password) {

        return UserDto.builder()
                   .email(email)
                   .password(password)
                   .build();
    }
}

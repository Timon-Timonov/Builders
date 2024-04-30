package it.academy.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailOccupaidException extends Exception {

    private final String message;
}

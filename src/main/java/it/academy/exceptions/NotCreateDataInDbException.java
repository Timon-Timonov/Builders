package it.academy.exceptions;

import lombok.Getter;

@Getter
public class NotCreateDataInDbException extends Exception {

    private String message;
}

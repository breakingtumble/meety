package com.example.meety1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InviteAlreadyExistsException extends RuntimeException{
    public InviteAlreadyExistsException(String message) {
        super(message);
    }
}

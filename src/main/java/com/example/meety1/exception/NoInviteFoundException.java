package com.example.meety1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class NoInviteFoundException extends RuntimeException {
    public NoInviteFoundException(String message) {
        super(message);
    }
}

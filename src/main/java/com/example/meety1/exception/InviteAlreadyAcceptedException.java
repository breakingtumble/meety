package com.example.meety1.exception;

public class InviteAlreadyAcceptedException extends RuntimeException {
    public InviteAlreadyAcceptedException(String message) {
        super(message);
    }
}

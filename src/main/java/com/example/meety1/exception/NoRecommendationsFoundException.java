package com.example.meety1.exception;

public class NoRecommendationsFoundException extends RuntimeException {
    public NoRecommendationsFoundException(String message) {
        super(message);
    }
}

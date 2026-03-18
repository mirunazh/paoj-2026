package com.pao.laboratory03.exception;

public class InvalidGradeException extends RuntimeException {
    public InvalidGradeException(String message) {
        super(message);
    }
}
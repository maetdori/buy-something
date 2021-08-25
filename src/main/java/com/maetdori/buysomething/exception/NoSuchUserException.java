package com.maetdori.buysomething.exception;

import java.util.NoSuchElementException;

public class NoSuchUserException extends NoSuchElementException {
    public NoSuchUserException() {
        super();
    }
    public NoSuchUserException(String message) {
        super(message);
    }
}

package io.github.mathieusoysal.exceptions;

public abstract class RobotoException extends Exception {

    public RobotoException(String message) {
        super(message);
    }

    public abstract String getErrorType();

}

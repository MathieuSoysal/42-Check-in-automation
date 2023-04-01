package io.github.mathieusoysal.exceptions;

public class RefusedConnectionException extends Exception {

    public RefusedConnectionException() {
        super("Connection refused, please check your login and password.");
    }

}

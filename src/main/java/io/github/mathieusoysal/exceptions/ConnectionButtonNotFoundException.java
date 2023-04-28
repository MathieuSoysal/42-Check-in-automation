package io.github.mathieusoysal.exceptions;

public class ConnectionButtonNotFoundException extends RobotoException {

    private static final long serialVersionUID = 1L;

    public ConnectionButtonNotFoundException(String url, String selector) {
        super("Connection button not found on " + url + " with selector " + selector);
    }

}

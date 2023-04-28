package io.github.mathieusoysal.exceptions;

public class EmailFieldNotFoundException extends RobotoException {

    private static final long serialVersionUID = 1L;

    public EmailFieldNotFoundException(String url, String cssSelector) {
        super("Email field not found on: " + url + " with CSS selector:" + cssSelector);
    }

}

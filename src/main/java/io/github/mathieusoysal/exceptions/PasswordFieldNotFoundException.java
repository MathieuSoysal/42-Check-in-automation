package io.github.mathieusoysal.exceptions;

public class PasswordFieldNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    public PasswordFieldNotFoundException(String url, String cssSelector) {
        super("Password field not found on: " + url + " with CSS selector:" + cssSelector);
    }
}

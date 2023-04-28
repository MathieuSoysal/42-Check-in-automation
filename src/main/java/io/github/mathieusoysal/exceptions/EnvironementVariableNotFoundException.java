package io.github.mathieusoysal.exceptions;

public class EnvironementVariableNotFoundException extends Exception {

    public EnvironementVariableNotFoundException(String... names) {
        super("Please set the following environment variable"
                + (names.length > 1 ? "s" : "") + ": " + String.join(", ", names));
    }

}

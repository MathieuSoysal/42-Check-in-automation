package io.github.mathieusoysal.exceptions;

public class EnvironementVariableNotFoundException extends Exception {

    public EnvironementVariableNotFoundException(String name) {
        super("Please set the " + name + " environment variable");
    }

}

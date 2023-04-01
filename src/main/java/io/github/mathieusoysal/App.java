package io.github.mathieusoysal;

import io.github.mathieusoysal.exceptions.RobotoException;

public class App {

    public static void main(String[] args) {
        try (Roboto roboto = new Roboto()) {
            roboto.connect("email", "password");
        } catch (RobotoException e) {
            System.out.println(e.getMessage());
        }
    }

}

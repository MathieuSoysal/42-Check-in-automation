package io.github.mathieusoysal;

import io.github.mathieusoysal.exceptions.RobotoException;

public class App {

    public static void main(String[] args) {
        try (Roboto roboto = new Roboto()) {
            roboto.connect(System.getenv("TEST_EMAIL"), System.getenv("TEST_PASSWORD"));
            while (!roboto.checkinButtonIsPresent())
                roboto.refreshPage();
            roboto.clickOnSubscription();
        } catch (RobotoException e) {
            System.out.println(e.getMessage());
        }
    }

}

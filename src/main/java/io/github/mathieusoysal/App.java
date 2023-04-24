package io.github.mathieusoysal;

import com.github.forax.beautifullogger.Logger;

import io.github.mathieusoysal.exceptions.EnvironementVariableNotFoundException;
import io.github.mathieusoysal.exceptions.RobotoException;

public class App {

    private static final Logger LOGGER = Logger.getLogger();

    public static void main(String[] args) {
        try {
            checkEnvVariable("TEST_EMAIL");
            checkEnvVariable("TEST_PASSWORD");
        } catch (EnvironementVariableNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
        }
        try (Roboto roboto = new Roboto()) {
            roboto.connect(System.getenv("TEST_EMAIL"), System.getenv("TEST_PASSWORD"));
            while (!roboto.checkinButtonIsPresent())
                roboto.refreshPage();
            roboto.clickOnSubscription();
        } catch (RobotoException e) {
            System.out.println(e.getMessage());
        }
    }

    static void checkEnvVariable(String name) throws EnvironementVariableNotFoundException {
        if (System.getenv(name) == null) {
            throw new EnvironementVariableNotFoundException(name);
        }
    }

}

package io.github.mathieusoysal;

import java.util.ArrayList;

import com.github.forax.beautifullogger.Logger;

import io.github.mathieusoysal.exceptions.EnvironementVariableNotFoundException;
import io.github.mathieusoysal.exceptions.RobotoException;

public class App {

    private static final String ENV_NAME_PASSWORD = "TEST_PASSWORD";
    private static final String ENV_NAME_MAIL = "TEST_EMAIL";
    private static final Logger LOGGER = Logger.getLogger();

    public static void main(String[] args) {
        checkAllEnvVariables(ENV_NAME_MAIL, ENV_NAME_PASSWORD);
        try (Roboto roboto = new Roboto()) {
            roboto.connect(System.getenv(ENV_NAME_MAIL), System.getenv(ENV_NAME_PASSWORD));
            while (!roboto.checkinButtonIsPresent())
                roboto.refreshPage();
            roboto.clickOnSubscription();
        } catch (RobotoException e) {
            System.out.println(e.getMessage());
        }
    }

    static void checkAllEnvVariables(String... envNames) {
        var notFound = new ArrayList<String>();
        for (String name : envNames) {
            try {
                checkEnvVariable(name);
            } catch (EnvironementVariableNotFoundException e) {
                LOGGER.error(e.getMessage(), e);
                notFound.add(name);
            }
        }
        if (notFound.size() > 0) {
            throw new RuntimeException(
                    "Please set the following environment variables: " + String.join(", ", notFound));
        }
    }

    static void checkEnvVariable(String name) throws EnvironementVariableNotFoundException {
        if (System.getenv(name) == null) {
            throw new EnvironementVariableNotFoundException(name);
        }
    }

}

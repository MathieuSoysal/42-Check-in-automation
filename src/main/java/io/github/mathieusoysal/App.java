package io.github.mathieusoysal;

import java.util.ArrayList;

import com.github.forax.beautifullogger.Logger;

import io.github.mathieusoysal.exceptions.EnvironementVariableNotFoundException;
import io.github.mathieusoysal.exceptions.RobotoException;

public class App {

    private static final String ENV_NAME_PASSWORD = "TEST_PASSWORD";
    private static final String ENV_NAME_MAIL = "TEST_EMAIL";
    private static final Logger LOGGER = Logger.getLogger();
    private static double startTime;

    public static void main(String[] args) {
        try (Roboto roboto = new Roboto()) {
            checkAllEnvVariables(ENV_NAME_MAIL, ENV_NAME_PASSWORD);
            roboto.connect(System.getenv(ENV_NAME_MAIL), System.getenv(ENV_NAME_PASSWORD));
            roboto.refreshPage();
            while (!roboto.checkinButtonIsPresent() && !fiftenMinutesPassed())
                roboto.refreshPage();
        } catch (RobotoException e) {
            System.out.println(e.getMessage());
        } catch (EnvironementVariableNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            System.exit(1);
        }
    }

    static boolean fiftenMinutesPassed() {
        if (startTime == 0)
            startTime = System.currentTimeMillis();
        return System.currentTimeMillis() - startTime > 1.5 * 60 * 1000;
    }

    static void checkAllEnvVariables(String... envNames) throws EnvironementVariableNotFoundException {
        var notFound = new ArrayList<String>();
        for (String name : envNames) {
            if (System.getenv(name) == null)
                notFound.add(name);
        }
        if (!notFound.isEmpty()) {
            throw new EnvironementVariableNotFoundException(notFound.toArray(new String[0]));
        }
    }

}

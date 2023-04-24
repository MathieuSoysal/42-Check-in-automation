package io.github.mathieusoysal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import io.github.mathieusoysal.exceptions.EnvironementVariableNotFoundException;

public class AppTest {

    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    @Test
    void testCheckEnvVariable() {
        assertThrows(EnvironementVariableNotFoundException.class, () -> {
            App.checkEnvVariable("none");
        });
    }

    @Test
    void testCheckEnvVariable2() {
        assertDoesNotThrow(() -> {
            App.checkEnvVariable("TEST_EMAIL");
        });
    }

}

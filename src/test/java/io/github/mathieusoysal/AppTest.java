package io.github.mathieusoysal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import io.github.mathieusoysal.exceptions.EnvironementVariableNotFoundException;

public class AppTest {

    @Test
    void testCheckAllEnvVariables() {
        assertDoesNotThrow(() -> {
            App.checkAllEnvVariables("TEST_EMAIL", "TEST_PASSWORD");
        });
    }

    @Test
    void testCheckAllEnvVariables2() {
        assertThrows(EnvironementVariableNotFoundException.class, () -> {
            App.checkAllEnvVariables("null", "nulls");
        });
    }

    @Test
    void testCheckAllEnvVariables3() {
        assertThrows(EnvironementVariableNotFoundException.class, () -> {
            App.checkAllEnvVariables("null");
        });
    }

}

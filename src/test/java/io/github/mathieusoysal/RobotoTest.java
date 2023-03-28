package io.github.mathieusoysal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

class RobotoTest {

    @Test
    void initTest() {
        assertDoesNotThrow(() -> {
            Roboto roboto = new Roboto();
            roboto.close();
        });
    }
}

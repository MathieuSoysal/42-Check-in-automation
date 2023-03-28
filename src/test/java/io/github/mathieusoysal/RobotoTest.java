package io.github.mathieusoysal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.junit.jupiter.api.Test;

import com.microsoft.playwright.Page;

class RobotoTest {

    @Test
    void initTest() {
        assertDoesNotThrow(() -> {
            Roboto roboto = new Roboto();
            roboto.close();
        });
    }

    @Test
    void connection_withEmail_test() {
        Roboto roboto = new Roboto();
        assertDoesNotThrow(() -> {
            roboto.connection("email", "password");
        });
        Page page = roboto.getPage();
        assertThat(page.locator("input[type='email']").first()).hasValue("email");
        roboto.close();
    }

    @Test
    void connection_withPassword_test() {
        Roboto roboto = new Roboto();
        assertDoesNotThrow(() -> {
            roboto.connection("email", "password");
        });
        Page page = roboto.getPage();
        assertThat(page.locator("input[type='password']").first()).hasValue("password");
        roboto.close();
    }

}

package io.github.mathieusoysal;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.microsoft.playwright.Page;

import io.github.mathieusoysal.exceptions.EmailFieldNotFoundException;
import io.github.mathieusoysal.exceptions.PasswordFieldNotFoundException;

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

    @Test
    void emailConnection_witWrongURL_test() {
        Roboto roboto = new Roboto("https://playwright.dev");
        assertThrows(EmailFieldNotFoundException.class, () -> {
            roboto.fillEmailField("email");
        });
        roboto.close();
    }

    @Test
    void passwordConnection_witWrongURL_test() {
        Roboto roboto = new Roboto("https://playwright.dev");
        assertThrows(PasswordFieldNotFoundException.class, () -> {
            roboto.fillPasswordField("password");
        });
        roboto.close();
    }

}

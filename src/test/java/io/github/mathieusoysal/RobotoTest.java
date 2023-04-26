package io.github.mathieusoysal;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.microsoft.playwright.Page;

import io.github.mathieusoysal.exceptions.ConnectionButtonNotFoundException;
import io.github.mathieusoysal.exceptions.EmailFieldNotFoundException;
import io.github.mathieusoysal.exceptions.PasswordFieldNotFoundException;
import io.github.mathieusoysal.exceptions.RefusedConnectionException;

class RobotoTest {

    @Test
    void initTest() {
        assertDoesNotThrow(() -> {
            Roboto roboto = new Roboto(false);
            roboto.close();
        });
    }

    @Test
    void connection_withEmail_test() {
        Roboto roboto = new Roboto(false);
        assertDoesNotThrow(() -> {
            roboto.fillEmailField("email");
        });
        Page page = roboto.getPage();
        assertThat(page.locator("input[type='email']").first()).hasValue("email");
        roboto.close();
    }

    @Test
    void connection_withPassword_test() {
        Roboto roboto = new Roboto(false);
        assertDoesNotThrow(() -> {
            roboto.fillPasswordField("password");
        });
        Page page = roboto.getPage();
        assertThat(page.locator("input[type='password']").first()).hasValue("password");
        roboto.close();
    }

    @Test
    void emailConnection_witWrongURL_test() {
        Roboto roboto = new Roboto("https://playwright.dev", false);
        roboto.getPage().setDefaultTimeout(1000);
        assertThrows(EmailFieldNotFoundException.class, () -> {
            roboto.fillEmailField("email");
        });
        roboto.close();
    }

    @Test
    void passwordConnection_witWrongURL_test() {
        Roboto roboto = new Roboto("https://playwright.dev", false);
        roboto.getPage().setDefaultTimeout(1000);
        assertThrows(PasswordFieldNotFoundException.class, () -> {
            roboto.fillPasswordField("password");
        });
        roboto.close();
    }

    @Test
    void clickSubmitButton_withBadLogin_test() throws InterruptedException, EmailFieldNotFoundException,
            PasswordFieldNotFoundException, ConnectionButtonNotFoundException, RefusedConnectionException {
        Roboto roboto = new Roboto(false);
        assertThrows(RefusedConnectionException.class, () -> {
            roboto.connect("email", "password");
        });
        assertThat(roboto.getPage().locator("div[role='alert']").first()).hasText(
                """
                        email ou mot de passe incorrect.

                        ×

                        """);
        roboto.close();
    }

    @Test
    void clickSubmitButton_withGoodLogin_test()
            throws EmailFieldNotFoundException, PasswordFieldNotFoundException, ConnectionButtonNotFoundException,
            RefusedConnectionException {
        Roboto roboto = new Roboto(false);
        assertNotEquals("42 à Paris | Candidats De La Présentation", roboto.getPage().title());
        roboto.connect(System.getenv("TEST_EMAIL"), System.getenv("TEST_PASSWORD"));
        assertEquals("42 à Paris | Candidats De La Présentation", roboto.getPage().title());
        roboto.close();
    }

    @Test
    void clickSubmitButton_witWrongURL_test() {
        Roboto roboto = new Roboto("https://playwright.dev", false);
        roboto.getPage().setDefaultTimeout(1000);
        assertThrows(ConnectionButtonNotFoundException.class, () -> {
            roboto.clickSubmitButton();
        });
        roboto.close();
    }

    @Test
    void captachaTest() throws EmailFieldNotFoundException, PasswordFieldNotFoundException,
            ConnectionButtonNotFoundException, RefusedConnectionException {
        Roboto roboto = new Roboto("https://www.google.com/recaptcha/api2/demo");
        roboto.getPage().setDefaultTimeout(6000);
        roboto.validateCaptcha();
        roboto.close();
        // TODO: find a way to test the captcha
    }

    @Test
    void testCheckinButtonIsNotPresent() throws EmailFieldNotFoundException, PasswordFieldNotFoundException,
            ConnectionButtonNotFoundException, RefusedConnectionException {
        Roboto roboto = new Roboto(false);
        roboto.getPage().setDefaultTimeout(1000);
        roboto.connect(System.getenv("TEST_EMAIL"), System.getenv("TEST_PASSWORD"));
        assertFalse(roboto.checkinButtonIsPresent());
        assertFalse(roboto.checkinButtonIsPresent());
    }

}

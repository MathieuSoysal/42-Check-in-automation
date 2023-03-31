package io.github.mathieusoysal;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;

import io.github.mathieusoysal.exceptions.ConnectionButtonNotFoundException;
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
            roboto.fillEmailField("email");
        });
        Page page = roboto.getPage();
        assertThat(page.locator("input[type='email']").first()).hasValue("email");
        roboto.close();
    }

    @Test
    void connection_withPassword_test() {
        Roboto roboto = new Roboto();
        assertDoesNotThrow(() -> {
            roboto.fillPasswordField("password");
        });
        Page page = roboto.getPage();
        assertThat(page.locator("input[type='password']").first()).hasValue("password");
        roboto.close();
    }

    @Test
    void emailConnection_witWrongURL_test() {
        Roboto roboto = new Roboto("https://playwright.dev");
        roboto.getPage().setDefaultTimeout(1000);
        assertThrows(EmailFieldNotFoundException.class, () -> {
            roboto.fillEmailField("email");
        });
        roboto.close();
    }

    @Test
    void passwordConnection_witWrongURL_test() {
        Roboto roboto = new Roboto("https://playwright.dev");
        roboto.getPage().setDefaultTimeout(1000);
        assertThrows(PasswordFieldNotFoundException.class, () -> {
            roboto.fillPasswordField("password");
        });
        roboto.close();
    }

    @Test
    void clickSubmitButton_withBadLogin_test() throws InterruptedException, EmailFieldNotFoundException,
            PasswordFieldNotFoundException, ConnectionButtonNotFoundException {
        Roboto roboto = new Roboto();
        roboto.connection("email", "password");
        assertThat(roboto.getPage().locator("div[role='alert']").first()).hasText(
                """
                        email ou mot de passe incorrect.

                        ×

                        """);
        roboto.close();
    }

    @Test
    void clickSubmitButton_withGoodLogin_test()
            throws EmailFieldNotFoundException, PasswordFieldNotFoundException, ConnectionButtonNotFoundException {
        Roboto roboto = new Roboto();
        assertNotEquals("42 à Paris | Candidats De La Présentation", roboto.getPage().title());
        roboto.connection(System.getenv("TEST_EMAIL"), System.getenv("TEST_PASSWORD"));
        assertEquals("42 à Paris | Candidats De La Présentation", roboto.getPage().title());
        roboto.close();
    }

    @Test
    void clickSubmitButton_witWrongURL_test() {
        Roboto roboto = new Roboto("https://playwright.dev");
        roboto.getPage().setDefaultTimeout(1000);
        assertThrows(ConnectionButtonNotFoundException.class, () -> {
            roboto.clickSubmitButton();
        });
        roboto.close();
    }

}

package io.github.mathieusoysal;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.microsoft.playwright.Page;

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
    void clickSubmitButton_test() throws InterruptedException {
        Roboto roboto = new Roboto();
        assertDoesNotThrow(() -> {
            roboto.connection("email", "password");
            roboto.clickSubmitButton();
        });
        assertThat(roboto.getPage().locator("div[role='alert']").first()).hasText(
                """
                        email ou mot de passe incorrect.

                        ×

                        """);
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

    @Test
    void clickSubmitButton_withGoodLogin_test() {
        Roboto roboto = new Roboto();
        assertDoesNotThrow(() -> {
            roboto.connection(System.getenv("TEST_EMAIL"), System.getenv("TEST_PASSWORD"));
            roboto.clickSubmitButton();
        });
        assertEquals("42 à Paris | Candidats De La Présentation", roboto.getPage().title());
        roboto.close();
    }

}

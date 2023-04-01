package io.github.mathieusoysal;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.TimeoutError;

import io.github.mathieusoysal.exceptions.ConnectionButtonNotFoundException;
import io.github.mathieusoysal.exceptions.EmailFieldNotFoundException;
import io.github.mathieusoysal.exceptions.PasswordFieldNotFoundException;
import io.github.mathieusoysal.exceptions.RefusedConnectionException;

/**
 * Bot to automate the 42 admissions process
 * 
 * 
 */
public class Roboto implements AutoCloseable {

    private static final String URL = "https://admissions.42.fr";

    private final String admissionURL;

    private Page page;

    private Playwright playwright;

    private Browser browser;

    public Roboto(final String admissionURL) {
        this.admissionURL = admissionURL;
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
        page = browser.newPage();
        page.navigate(admissionURL);
    }

    public Roboto() {
        this(URL);
    }

    public void connection(String email, String password)
            throws EmailFieldNotFoundException, PasswordFieldNotFoundException, ConnectionButtonNotFoundException, RefusedConnectionException {
        String stateBeforeConnection = page.url();
        fillEmailField(email);
        fillPasswordField(password);
        clickSubmitButton();
        page.waitForLoadState();
        String stateAfterConnection = page.url();
        if (stateBeforeConnection.equals(stateAfterConnection)) {
            throw new RefusedConnectionException();
        }
    }

    void fillEmailField(String email) throws EmailFieldNotFoundException {
        String selector = "input[type='email']";
        try {
            page.locator(selector).first().fill(email);
        } catch (TimeoutError e) {
            throw new EmailFieldNotFoundException(admissionURL, selector);
        }
    }

    void fillPasswordField(String password) throws PasswordFieldNotFoundException {
        String selector = "input[type='password']";
        try {
            page.locator(selector).first().fill(password);
        } catch (TimeoutError e) {
            throw new PasswordFieldNotFoundException(admissionURL, selector);
        }
    }

    @Override
    public void close() {
        browser.close();
        playwright.close();
    }

    public Page getPage() {
        return page;
    }

    void clickSubmitButton() throws ConnectionButtonNotFoundException {
        String selector = "[type='submit']";
        try {
            page.locator(selector).first().click();
        } catch (TimeoutError e) {
            throw new ConnectionButtonNotFoundException(admissionURL, selector);
        }
    }

}
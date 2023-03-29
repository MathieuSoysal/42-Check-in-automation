package io.github.mathieusoysal;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.TimeoutError;

import io.github.mathieusoysal.exceptions.EmailFieldNotFoundException;
import io.github.mathieusoysal.exceptions.PasswordFieldNotFoundException;

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
            throws EmailFieldNotFoundException, PasswordFieldNotFoundException {
        page.setDefaultTimeout(5000);
        fillEmailField(email);
        fillPasswordField(password);
    }

    void fillEmailField(String email) throws EmailFieldNotFoundException {
        try {
            page.locator("input[type='email']").first().fill(email);
        } catch (TimeoutError e) {
            throw new EmailFieldNotFoundException(admissionURL, "input[type='email']");
        }
    }

    void fillPasswordField(String password) throws PasswordFieldNotFoundException {
        try {
            page.locator("input[type='password']").first().fill(password);
        } catch (TimeoutError e) {
            throw new PasswordFieldNotFoundException(admissionURL, "input[type='password']");
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

}
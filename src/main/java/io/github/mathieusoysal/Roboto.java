package io.github.mathieusoysal;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.Tracing;

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

    private BrowserContext context;

    private String trace = "Archive";

    private boolean telemetry = true;

    public Roboto(final String admissionURL, final boolean telemetry) {
        this.telemetry = telemetry;
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
        if (telemetry) {
            context = browser.newContext();
            context.tracing().start(new Tracing.StartOptions()
                    .setScreenshots(true)
                    .setSnapshots(true));
            page = context.newPage();
        } else {
            page = browser.newPage();
        }
        this.admissionURL = admissionURL;
        page.navigate(admissionURL);
    }

    public Roboto(String admissionURL) {
        this(admissionURL, true);
    }

    public Roboto(final boolean telemetry) {
        this(URL, telemetry);
    }

    public Roboto() {
        this(true);
    }

    public void connect(String email, String password)
            throws EmailFieldNotFoundException, PasswordFieldNotFoundException, ConnectionButtonNotFoundException,
            RefusedConnectionException {
        String stateBeforeConnection = page.url();
        fillEmailField(email);
        fillPasswordField(password);
        clickSubmitButton();
        page.waitForLoadState();
        String stateAfterConnection = page.url();
        if (stateBeforeConnection.equals(stateAfterConnection)) {
            trace = "RefusedConnection";
            throw new RefusedConnectionException();
        }
    }

    @Override
    public void close() {
        if (telemetry) {
            String archiveName = trace + "-" + LocalDateTime.now(ZoneId.systemDefault()).toString().replaceAll(":", "-") + ".zip";
            context.tracing().stop(new Tracing.StopOptions()
                    .setPath(Paths.get(archiveName)));
            context.close();
        }
        browser.close();
        playwright.close();
    }

    public Page getPage() {
        return page;
    }

    void fillEmailField(String email) throws EmailFieldNotFoundException {
        String selector = "input[type='email']";
        try {
            page.locator(selector).first().fill(email);
        } catch (TimeoutError e) {
            trace = "EmailFieldNotFound";
            throw new EmailFieldNotFoundException(admissionURL, selector);
        }
    }

    void fillPasswordField(String password) throws PasswordFieldNotFoundException {
        String selector = "input[type='password']";
        try {
            page.locator(selector).first().fill(password);
        } catch (TimeoutError e) {
            trace = "PasswordFieldNotFound";
            throw new PasswordFieldNotFoundException(admissionURL, selector);
        }
    }

    void clickSubmitButton() throws ConnectionButtonNotFoundException {
        String selector = "[type='submit']";
        try {
            page.locator(selector).first().click();
        } catch (TimeoutError e) {
            trace = "ConnectionButtonNotFound";
            throw new ConnectionButtonNotFoundException(admissionURL, selector);
        }
    }

}
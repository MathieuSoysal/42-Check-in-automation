package io.github.mathieusoysal;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;

import com.github.forax.beautifullogger.Logger;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
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

    private static final Logger LOGGER = Logger.getLogger();

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
        LOGGER.info(() -> "Roboto is ready to connect to " + admissionURL);
        page.navigate(admissionURL);
        LOGGER.info(() -> "Roboto is connected to " + admissionURL);
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
            LOGGER.error(() -> "Connection refused");
            throw new RefusedConnectionException();
        }
        LOGGER.info(() -> "Roboto is signed in to " + admissionURL);
    }

    public void refreshPage() {
        page.reload();
        LOGGER.info(() -> "Page refreshed");
    }

    public boolean checkinButtonIsPresent() {
        page.waitForLoadState();
        return page.locator(
                "input[type='submit'] :not([value='Enregistrement impossible']) :not([value='Can not subscribe'])")
                .last().isVisible();
    }

    public void subcribeToCheckIn() {
        validateCaptcha();
        clickOnSubscription();
    }

    @Override
    public void close() {
        if (telemetry) {
            String archiveName = trace + "-" + LocalDateTime.now(ZoneId.systemDefault()).toString().replaceAll(":", "-")
                    + ".zip";
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
            LOGGER.error("Email field not found", e);
            throw new EmailFieldNotFoundException(admissionURL, selector);
        }
        LOGGER.info(() -> "Email filled");
    }

    void fillPasswordField(String password) throws PasswordFieldNotFoundException {
        String selector = "input[type='password']";
        try {
            page.locator(selector).first().fill(password);
            LOGGER.info(() -> "Password filled");
        } catch (TimeoutError e) {
            trace = "PasswordFieldNotFound";
            LOGGER.error("Password field not found", e);
            throw new PasswordFieldNotFoundException(admissionURL, selector);
        }
    }

    void clickSubmitButton() throws ConnectionButtonNotFoundException {
        String selector = "[type='submit']";
        try {
            page.locator(selector).first().click();
            LOGGER.info(() -> "Submit button clicked");
        } catch (TimeoutError e) {
            trace = "ConnectionButtonNotFound";
            LOGGER.error("Connection button not found", e);
            throw new ConnectionButtonNotFoundException(admissionURL, selector);
        }
    }

    void validateCaptcha() {
        page.locator("body").click();
        page.locator("body").press("Tab");
        page.locator("body").press("Enter");
        page.waitForLoadState();
        LOGGER.info(() -> "Captcha validated");
        // TODO: Add a check to see if the captcha is present
    }

    void clickOnSubscription() {
        page.locator("input[type='submit'] :not([value='Enregistrement impossible']) :not([value='Can not subscribe'])")
                .last().click();
        LOGGER.info(() -> "Subscription clicked");
        // TODO: Add a check to see if the subscription is successful
    }

}
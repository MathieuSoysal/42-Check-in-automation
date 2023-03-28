package io.github.mathieusoysal;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

/**
 * Bot to automate the 42 admissions process
 * 
 * 
 */
public class Roboto implements AutoCloseable {

    private static final String URL = "https://admissions.42.fr";

    private Page page;

    private Playwright playwright;

    private Browser browser;

    public Roboto(final String url) {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
        page = browser.newPage();
        page.navigate(url);
    }

    public Roboto() {
        this(URL);
    }

    public void connection(String email, String password) {
        page.locator("input[type='email']").first().fill(email);
        page.locator("input[type='password']").first().fill(password);
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

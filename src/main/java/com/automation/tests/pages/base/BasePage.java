package com.automation.tests.pages.base;

import com.automation.tests.driver.DriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *  Shared helper for every page-object.
 *  - Centralises waits, JS helpers, scrolling, dropdown utils
 *  - Makes timeouts & baseUrl configurable via system-property or config file
 *  - Adds soft-retry click (JS fallback) and highlight() for easy debugging
 */
public abstract class BasePage {

    /* ------------ configuration ------------ */
    private static final int DEFAULT_TIMEOUT  = Integer.parseInt(
            System.getProperty("ui.timeout", "10"));        // -Dui.timeout=15
    private static final String BASE_URL      = System.getProperty("ui.baseUrl",
            "https://example.com"); /* override on CLI or CI */

    /* ------------ driver / tooling ------------ */
    protected final Logger           log     = LoggerFactory.getLogger(getClass());
    protected final WebDriver        driver  = DriverManager.getDriver();
    protected final WebDriverWait    wait    = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
    protected final WebDriverWait    shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
    protected final WebDriverWait    longWait  = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT * 3L));
    protected final Actions          actions = new Actions(driver);
    protected final JavascriptExecutor js     = (JavascriptExecutor) driver;

    /* ======================================================================== */
    /*                               NAVIGATION                                 */
    /* ======================================================================== */
    public void open(String relativePath) {
        String url = relativePath.startsWith("http") ? relativePath : BASE_URL + relativePath;
        log.info("â†’ Open URL: {}", url);
        driver.get(url);
        waitForPageLoad();
    }

    public void refresh()          { log.debug("â†» Refresh"); driver.navigate().refresh(); }
    public String currentUrl()     { return driver.getCurrentUrl(); }
    public String title()          { return driver.getTitle();      }

    /* ======================================================================== */
    /*                               WAIT HELPERS                               */
    /* ======================================================================== */
    protected <T> T wait(Function<WebDriver, T> condition)            { return wait.until(condition); }
    protected WebElement visible(By by)                               { return wait(ExpectedConditions.visibilityOfElementLocated(by)); }
    protected WebElement visible(WebElement e)                        { return wait(ExpectedConditions.visibilityOf(e)); }
    protected WebElement clickable(By by)                             { return wait(ExpectedConditions.elementToBeClickable(by)); }
    protected WebElement clickable(WebElement e)                      { return wait(ExpectedConditions.elementToBeClickable(e)); }
    protected void        waitForPageLoad()                           { wait(d -> js.executeScript("return document.readyState").equals("complete")); }

    /* ======================================================================== */
    /*                          BASIC ELEMENT ACTIONS                           */
    /* ======================================================================== */
    protected void click(By by)               { click(clickable(by)); }
    protected void click(WebElement e) {
        try { clickable(e).click(); }
        catch (ElementClickInterceptedException ex) {
            log.debug("JS fallback click â–º {}", ex.getMessage());
            highlight(e);  js.executeScript("arguments[0].click();", e);
        }
    }
    protected void type(By by, String txt)    { type(visible(by), txt); }
    protected void type(WebElement e, String txt) {
        WebElement v = visible(e);  v.clear();
        if (txt != null) v.sendKeys(txt);
    }

    protected String text(By by)              { return visible(by).getText();    }
    protected String text(WebElement e)       { return visible(e).getText();     }
    protected String attr(WebElement e,String a){ return visible(e).getAttribute(a); }

    /* ======================================================================== */
    /*                          STATE / ASSERT HELPERS                          */
    /* ======================================================================== */
    protected boolean displayed(By by)        { return safe(() -> driver.findElement(by).isDisplayed()); }
    protected boolean displayed(WebElement e) { return safe(e::isDisplayed); }
    protected boolean enabled (WebElement e)  { return safe(e::isEnabled);  }
    private boolean safe(java.util.concurrent.Callable<Boolean> c) {
        try { return c.call(); }
        catch (Exception ex) { return false; }
    }

    /* ======================================================================== */
    /*                          DROPDOWN / ADVANCED                             */
    /* ======================================================================== */
    protected void selectByVisibleText(WebElement d,String text) { new Select(visible(d)).selectByVisibleText(text); }
    protected void selectByValue      (WebElement d,String val ) { new Select(visible(d)).selectByValue(val); }
    protected void selectByIndex      (WebElement d,int idx    ) { new Select(visible(d)).selectByIndex(idx); }

    protected void scrollIntoView(WebElement e)  { js.executeScript("arguments[0].scrollIntoView({block:'center'})", e); }
    protected void hover(WebElement e)           { actions.moveToElement(visible(e)).perform(); }
    protected void dragAndDrop(WebElement src, WebElement dst) { actions.dragAndDrop(visible(src), visible(dst)).perform(); }

    /* ======================================================================== */
    /*                               UTILITIES                                  */
    /* ======================================================================== */
    protected byte[] screenshot() { return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES); }
    protected void   highlight(WebElement e) {
        js.executeScript("arguments[0].style.outline='3px solid #FF6600'", e);
    }

    /* ======================================================================== */
    /*                     LIST-ORIENTED CONVENIENCE METHODS                    */
    /* ======================================================================== */
    protected List<String> texts(List<WebElement> elements) {
        return elements.stream().map(this::text).collect(Collectors.toList());
    }
}

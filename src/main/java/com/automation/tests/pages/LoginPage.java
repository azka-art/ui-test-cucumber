package com.automation.tests.pages;

import com.automation.tests.pages.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage extends BasePage {
    
    // Halaman URL - sesuaikan dengan situs target
    private static final String LOGIN_PAGE_URL = "/login";
    
    // Sesuaikan selektori ini dengan halaman sebenarnya pada situs target
    @FindBy(id = "username")
    private WebElement usernameField;
    
    @FindBy(id = "password")
    private WebElement passwordField;
    
    @FindBy(css = "button[type='submit']")
    private WebElement loginButton;
    
    @FindBy(css = ".flash.error")
    private WebElement errorMessage;
    
    // Constructor
    public LoginPage() {
        PageFactory.initElements(driver, this);
    }
    
    // Action methods
    public LoginPage navigateToLoginPage() {
        String url = "https://the-internet.herokuapp.com" + LOGIN_PAGE_URL;
        log.info("Navigating to URL: {}", url);
        driver.get(url);
        
        // Debug info
        log.info("Current URL: {}", driver.getCurrentUrl());
        log.info("Page Title: {}", driver.getTitle());
        log.info("Page Source contains 'username': {}", driver.getPageSource().contains("username"));
        
        try {
            // Try to find element directly without waiting
            WebElement usernameDirectly = driver.findElement(By.id("username"));
            log.info("Username field found directly: {}", usernameDirectly != null);
        } catch (Exception e) {
            log.error("Could not find username field directly: {}", e.getMessage());
        }
        
        return this;
    }
    
    public LoginPage enterEmail(String email) {
        log.info("Current URL before entering email: {}", driver.getCurrentUrl());
        log.info("Attempting to type '{}' in username field", email);
        type(usernameField, email);
        log.info("Entered username: {}", email);
        return this;
    }
    
    public LoginPage enterPassword(String password) {
        type(passwordField, password);
        log.info("Entered password: ******");
        return this;
    }
    
    public DashboardPage clickLoginButton() {
        click(loginButton);
        log.info("Clicked login button");
        return new DashboardPage();
    }
    
    public DashboardPage loginAs(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        return clickLoginButton();
    }
    
    public LoginPage loginExpectingFailure(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        click(loginButton);
        return this;
    }
    
    public boolean isErrorMessageDisplayed() {
        try {
            boolean isDisplayed = displayed(errorMessage);
            log.info("Error message displayed: {}", isDisplayed);
            if (isDisplayed) {
                log.info("Error message text: {}", text(errorMessage));
            } else {
                log.info("Checking for all flash messages on page...");
                java.util.List<WebElement> flashes = driver.findElements(By.cssSelector(".flash"));
                for (WebElement flash : flashes) {
                    log.info("Flash message found: {}", flash.getText());
                }
            }
            return isDisplayed;
        } catch (Exception e) {
            log.error("Error checking for error message: {}", e.getMessage());
            return false;
        }
    }
    
    public String getErrorMessageText() {
        if (displayed(errorMessage)) {
            String message = text(errorMessage);
            log.info("Error message text: {}", message);
            return message;
        }
        log.warn("No error message found");
        return "";
    }
    
    public LoginPage checkRememberMe() {
        // Element tidak ada di halaman demo - fungsi tidak melakukan apa-apa untuk demo
        log.info("Remember Me not available on demo site");
        return this;
    }
    
    // Getter untuk akses dari step definition
    public WebElement getUsernameField() {
        return usernameField;
    }
    
    public String getEmailValue() {
        return attr(usernameField, "value");
    }
}
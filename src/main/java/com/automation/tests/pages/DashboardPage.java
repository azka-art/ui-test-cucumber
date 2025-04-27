package com.automation.tests.pages;

import com.automation.tests.pages.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class DashboardPage extends BasePage {
    
    // Untuk demo site, sesuaikan dengan elemen yang ada di halaman setelah login
    @FindBy(css = ".flash.success")
    private WebElement successMessage;
    
    @FindBy(css = "h2")
    private WebElement secureAreaHeading;
    
    @FindBy(css = "h4.subheader")
    private WebElement welcomeMessage;
    
    @FindBy(css = ".button.secondary.radius")
    private WebElement logoutButton;
    
    // Constructor
    public DashboardPage() {
        PageFactory.initElements(driver, this);
        log.info("Current URL after login: {}", driver.getCurrentUrl());
        log.info("Page title after login: {}", driver.getTitle());
        log.info("Page contains 'Secure Area': {}", driver.getPageSource().contains("Secure Area"));
    }
    
    public boolean isDashboardDisplayed() {
        boolean displayed = displayed(secureAreaHeading);
        log.info("Secure Area heading displayed: {}", displayed);
        return displayed;
    }
    
    public String getWelcomeMessageText() {
        if (displayed(welcomeMessage)) {
            return text(welcomeMessage);
        }
        return "Welcome message not found";
    }
    
    public String getSuccessMessage() {
        if (displayed(successMessage)) {
            return text(successMessage);
        }
        return "";
    }
    
    public LoginPage logout() {
        click(logoutButton);
        log.info("Clicked logout button");
        return new LoginPage();
    }
}
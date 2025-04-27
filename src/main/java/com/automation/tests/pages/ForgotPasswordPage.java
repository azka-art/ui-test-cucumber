package com.automation.tests.pages;

import com.automation.tests.pages.base.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ForgotPasswordPage extends BasePage {
    
    @FindBy(id = "email")
    private WebElement emailField;
    
    @FindBy(css = "button[type='submit']")
    private WebElement resetButton;
    
    @FindBy(css = ".alert-success")
    private WebElement successMessage;
    
    @FindBy(css = ".alert-danger")
    private WebElement errorMessage;
    
    @FindBy(linkText = "Back to Login")
    private WebElement backToLoginLink;
    
    public ForgotPasswordPage() {
        PageFactory.initElements(driver, this);
        visible(emailField);
        log.info("Forgot Password page loaded");
    }
    
    public ForgotPasswordPage enterEmail(String email) {
        type(emailField, email);
        log.info("Entered email: {}", email);
        return this;
    }
    
    public ForgotPasswordPage clickResetButton() {
        click(resetButton);
        log.info("Clicked reset button");
        return this;
    }
    
    public boolean isSuccessMessageDisplayed() {
        return displayed(successMessage);
    }
    
    public String getSuccessMessageText() {
        return text(successMessage);
    }
    
    public boolean isErrorMessageDisplayed() {
        return displayed(errorMessage);
    }
    
    public String getErrorMessageText() {
        return text(errorMessage);
    }
    
    public LoginPage navigateBackToLogin() {
        click(backToLoginLink);
        log.info("Clicked Back to Login link");
        return new LoginPage();
    }
    
    public ForgotPasswordPage requestPasswordReset(String email) {
        enterEmail(email);
        clickResetButton();
        return this;
    }
}

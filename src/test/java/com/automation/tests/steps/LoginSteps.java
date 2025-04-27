package com.automation.tests.steps;

import com.automation.tests.pages.DashboardPage;
import com.automation.tests.pages.ForgotPasswordPage;
import com.automation.tests.pages.LoginPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

public class LoginSteps {
    private static final Logger log = LoggerFactory.getLogger(LoginSteps.class);
    
    private LoginPage loginPage;
    private DashboardPage dashboardPage;
    private ForgotPasswordPage forgotPasswordPage;
    
    @Given("I am on the login page")
    public void iAmOnTheLoginPage() {
        loginPage = new LoginPage();
        loginPage.navigateToLoginPage();
    }
    
    @When("I enter email {string}")
    public void iEnterEmail(String email) {
        loginPage.enterEmail(email);
    }
    
    @And("I enter password {string}")
    public void iEnterPassword(String password) {
        loginPage.enterPassword(password);
    }
    
    @And("I click the login button")
    public void iClickTheLoginButton() {
        try {
            dashboardPage = loginPage.clickLoginButton();
        } catch (Exception e) {
            // If login fails, dashboardPage will be null
            log.info("Login failed with exception: {}", e.getMessage());
        }
    }
    
    @Then("I should be redirected to the dashboard")
    public void iShouldBeRedirectedToTheDashboard() {
        assertNotNull(dashboardPage, "Dashboard page should not be null");
        assertTrue(dashboardPage.isDashboardDisplayed(), "Dashboard page should be displayed");
    }
    
    @And("I should see a welcome message")
    public void iShouldSeeAWelcomeMessage() {
        String message = dashboardPage.getWelcomeMessageText();
        assertNotNull(message, "Welcome message should not be null");
        assertFalse(message.isEmpty(), "Welcome message should not be empty");
    }
    
    @Then("I should see an error message {string}")
    public void iShouldSeeAnErrorMessage(String errorMessage) {
        assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed");
        assertTrue(loginPage.getErrorMessageText().contains(errorMessage), 
                "Error message should contain: " + errorMessage);
    }
    
    @And("I should remain on the login page")
    public void iShouldRemainOnTheLoginPage() {
        assertTrue(loginPage.currentUrl().contains("/login"), "URL should contain /login");
    }
    
    @When("I login with {string} and {string}")
    public void iLoginWithAnd(String email, String password) {
        dashboardPage = loginPage.loginAs(email, password);
    }
    
    @And("I click the logout button on the dashboard")
    public void iClickTheLogoutButtonOnTheDashboard() {
        loginPage = dashboardPage.logout();
    }
    
    @And("I check the remember me checkbox")
    public void iCheckTheRememberMeCheckbox() {
        loginPage.checkRememberMe();
    }
    
    @And("I logout from the application")
    public void iLogoutFromTheApplication() {
        loginPage = dashboardPage.logout();
    }
    
    @And("I navigate back to the login page")
    public void iNavigateBackToTheLoginPage() {
        loginPage.navigateToLoginPage();
    }
    
    @Then("my email should be pre-filled with {string}")
    public void myEmailShouldBePreFilledWith(String email) {
        assertEquals(email, loginPage.getEmailValue(), 
                "Email field should be pre-filled with: " + email);
    }
}
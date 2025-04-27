Feature: User Authentication
  As a user of the application
  I want to be able to login to the application
  So that I can access protected features

  Background:
    Given I am on the login page

  @smoke @positive
  Scenario: Successful login with valid credentials
    When I enter email "tomsmith"
    And I enter password "SuperSecretPassword!"
    And I click the login button
    Then I should be redirected to the dashboard
    And I should see a welcome message

  @negative
  Scenario Outline: Failed login with invalid credentials
    When I enter email "<email>"
    And I enter password "<password>"
    And I click the login button
    Then I should see an error message "<error_message>"
    And I should remain on the login page

    Examples:
      | email    | password              | error_message                |
      | tomsmith | wrongpassword         | Your password is invalid!    |
      | wronguser| SuperSecretPassword!  | Your username is invalid!    |
      |          | SuperSecretPassword!  | Your username is invalid!    |
      | tomsmith |                       | Your password is invalid!    |
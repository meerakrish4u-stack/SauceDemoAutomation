Feature: SauceDemo E-Commerce Purchase Flow

  @Smoke @Regression
  Scenario: 1. Successful end-to-end purchase flow (Happy Path)
    Given User is on the SauceDemo login page
    When User logs in with valid username "standard_user" and password "secret_sauce"
    And User adds "Sauce Labs Backpack" to the cart
    And User navigates to the checkout page
    And User enters shipping details with first name "Meera", last name "Lalitha", and postal code "1500"
    And User clicks continue button
    And User completes the purchase
    Then Order confirmation header should display "Thank you for your order!"

  @Regression
  Scenario Outline: 2. Data-driven checkout validation with multiple user profiles
    Given User is on the SauceDemo login page
    When User logs in with valid username "<username>" and password "<password>"
    And User adds "Sauce Labs Bike Light" to the cart
    And User navigates to the checkout page
    And User enters shipping details with first name "<firstName>", last name "<lastName>", and postal code "<postalCode>"
    And User clicks continue button
    And User completes the purchase
    Then Order confirmation header should display "Thank you for your order!"

    Examples:
      | username                | password     | firstName | lastName | postalCode |
      | standard_user           | secret_sauce | Alice     | Smith    | 1000       |
      | performance_glitch_user | secret_sauce | Bob       | Jones    | 9000       |

  @Regression
  Scenario: 3. Add and remove items from cart
    Given User is on the SauceDemo login page
    When User logs in with valid username "standard_user" and password "secret_sauce"
    And User adds "Sauce Labs Backpack" to the cart
    Then Cart item count badge should display "1"
    When User removes "Sauce Labs Backpack" from the cart
    Then Cart item count badge should be hidden

  @Negative @Regression
  Scenario: 4. Negative test - Checkout attempt with missing postal code
    Given User is on the SauceDemo login page
    When User logs in with valid username "standard_user" and password "secret_sauce"
    And User adds "Sauce Labs Backpack" to the cart
    And User navigates to the checkout page
    And User enters shipping details with first name "Meera", last name "Lalitha", and postal code ""
    And User clicks continue button
    Then Error message should display "Error: Postal Code is required"
package com.qa.stepdefinitions;

import com.qa.pages.CheckoutPage;
import com.qa.pages.InventoryPage;
import com.qa.pages.LoginPage;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

public class PurchaseSteps {

    private WebDriver driver;
    private WebDriverWait wait;

    // Declaring Page Objects
    private LoginPage loginPage;
    private InventoryPage inventoryPage;
    private CheckoutPage checkoutPage;

    @Before
    public void setUp() {
        java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(java.util.logging.Level.OFF);

        System.out.println("--- SETUP START ---");
        ChromeOptions options = new ChromeOptions();


        // Prevent password breach alerts & save password popups
        java.util.Map<String, Object> prefs = new java.util.HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);
        options.setExperimentalOption("prefs", prefs);

        options.addArguments("--disable-save-password-bubble");
        options.addArguments("--disable-single-click-autofill");
        options.addArguments("--disable-notifications");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--start-maximized");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        // Instantiate Page Objects passing only 'driver'
        loginPage = new LoginPage(driver);
        inventoryPage = new InventoryPage(driver);
        checkoutPage = new CheckoutPage(driver);
    }

    private void visualPause(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Given("User is on the SauceDemo login page")
    public void user_is_on_the_sauce_demo_login_page() {
        loginPage.navigateToLogin("https://www.saucedemo.com/");
        visualPause(500);
    }

    @When("User logs in with valid username {string} and password {string}")
    public void user_logs_in_with_valid_username_and_password(String username, String password) {
        loginPage.login(username, password);
        visualPause(500);
    }

    @When("User adds {string} to the cart")
    public void user_adds_to_the_cart(String itemName) {
        inventoryPage.addProductToCart(itemName);
        visualPause(500);
    }

    @When("User removes {string} from the cart")
    public void user_removes_from_the_cart(String itemName) {
        inventoryPage.removeProductFromCart(itemName);
        visualPause(500);
    }

    @When("User navigates to the checkout page")
    public void user_navigates_to_the_checkout_page() {
        inventoryPage.clickCartIcon();
        visualPause(500);
        checkoutPage.clickCheckout();
        visualPause(500);
    }

    @When("User enters shipping details with first name {string}, last name {string}, and postal code {string}")
    public void user_enters_shipping_details_with_first_name_last_name_and_postal_code(String firstName, String lastName, String postalCode) {
        checkoutPage.enterShippingDetails(firstName, lastName, postalCode);
        visualPause(500);
    }

    @When("User clicks continue button")
    public void user_clicks_continue_button() {
        checkoutPage.clickContinue();
        visualPause(500);
    }

    @When("User completes the purchase")
    public void user_completes_the_purchase() {
        if (driver.getCurrentUrl().contains("checkout-step-one")) {
            checkoutPage.clickContinue();
            visualPause(500);
        }
        wait.until(ExpectedConditions.urlContains("checkout-step-two"));
        checkoutPage.clickFinish();
        visualPause(500);
    }

    @Then("Order confirmation header should display {string}")
    public void order_confirmation_header_should_display(String expectedHeader) {
        String actualHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("complete-header"))).getText();
        Assert.assertEquals(actualHeader, expectedHeader, "Order confirmation header mismatch.");
    }

    @Then("Cart item count badge should display {string}")
    public void cart_item_count_badge_should_display(String expectedCount) {
        Assert.assertEquals(inventoryPage.getCartItemCount(), expectedCount, "Cart count mismatch.");
    }

    @Then("Cart item count badge should be hidden")
    public void cart_item_count_badge_should_be_hidden() {
        Assert.assertTrue(inventoryPage.isCartBadgeHidden(), "Cart badge should not be visible.");
    }

    @Then("Error message should display {string}")
    public void error_message_should_display(String expectedError) {
        String actualError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h3[data-test='error']"))).getText();
        Assert.assertEquals(actualError, expectedError, "Error message mismatch.");
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
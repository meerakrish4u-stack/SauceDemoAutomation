package com.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.NoSuchElementException;

public class InventoryPage {
    private WebDriver driver;

    // 1. Locators
    private By cartIcon = By.cssSelector(".shopping_cart_link");
    private By cartBadge = By.cssSelector(".shopping_cart_badge");

    // Dynamic locators based on product name
    private By getProductButton(String productName) {
        // Finds the specific product container and targets its button (Add or Remove)
        return By.xpath("//div[text()='" + productName + "']/ancestor::div[@class='inventory_item_description']//button");
    }

    // 2. Constructor
    public InventoryPage(WebDriver driver) {
        this.driver = driver;
    }

    // 3. Page Actions
    public void addOrRemoveProduct(String productName) {
        driver.findElement(getProductButton(productName)).click();
    }

    // Alias methods to explicitly match step definitions
    public void addProductToCart(String productName) {
        addOrRemoveProduct(productName);
    }

    public void removeProductFromCart(String productName) {
        addOrRemoveProduct(productName);
    }

    public String getCartItemCount() {
        return driver.findElement(cartBadge).getText();
    }

    public boolean isCartBadgeHidden() {
        try {
            return !driver.findElement(cartBadge).isDisplayed();
        } catch (NoSuchElementException e) {
            return true; // Badge is successfully hidden/removed from DOM
        }
    }

    public void clickCartIcon() {
        driver.findElement(cartIcon).click();
    }
}
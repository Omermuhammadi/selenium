package com.blogapp.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.UUID;

/**
 * Test cases for user login functionality.
 * Tests: Login page load, invalid credentials, empty fields, navigation links
 */
public class LoginTest extends BaseTest {

    @Test(priority = 1)
    public void testLoginPageLoads() {
        System.out.println("\n🧪 Test 5: Login page loads correctly");
        navigateTo("/login");
        
        // Check page loaded with login content
        String pageSource = driver.getPageSource().toLowerCase();
        Assert.assertTrue(pageSource.contains("login") || pageSource.contains("sign in"),
                         "Login page should contain 'login' or 'sign in' text");
        
        // Check for form elements
        boolean hasEmailField = driver.findElements(By.name("email")).size() > 0 ||
                               driver.findElements(By.name("username")).size() > 0;
        Assert.assertTrue(hasEmailField, "Login form should have email/username field");
        
        boolean hasPasswordField = driver.findElements(By.name("password")).size() > 0;
        Assert.assertTrue(hasPasswordField, "Login form should have password field");
        
        System.out.println("✅ Test 5 PASSED: Login page loads correctly");
    }

    @Test(priority = 2)
    public void testLoginWithInvalidCredentials() {
        System.out.println("\n🧪 Test 6: Login with invalid credentials shows error");
        navigateTo("/login");
        
        // Enter invalid credentials
        WebElement emailField = driver.findElement(By.name("email"));
        WebElement passwordField = driver.findElement(By.name("password"));
        
        emailField.clear();
        emailField.sendKeys("invalid@email.com");
        passwordField.clear();
        passwordField.sendKeys("wrongpassword123");
        
        // Submit form
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();
        
        // Wait for response
        sleep(2000);
        
        // Should show error or stay on login page
        String currentUrl = driver.getCurrentUrl();
        String pageSource = driver.getPageSource().toLowerCase();
        
        boolean handledInvalidLogin = currentUrl.contains("/login") || 
                                      pageSource.contains("invalid") || 
                                      pageSource.contains("error") ||
                                      pageSource.contains("incorrect") ||
                                      pageSource.contains("failed");
        
        Assert.assertTrue(handledInvalidLogin,
                         "Should show error message or stay on login page for invalid credentials");
        
        System.out.println("✅ Test 6 PASSED: Invalid credentials handled correctly");
    }

    @Test(priority = 3)
    public void testLoginWithEmptyFields() {
        System.out.println("\n🧪 Test 7: Login with empty fields validation");
        navigateTo("/login");
        
        // Try to submit empty form
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();
        
        sleep(1000);
        
        // Should stay on login page due to validation
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("/login"),
                         "Should stay on login page with empty fields");
        
        System.out.println("✅ Test 7 PASSED: Empty field validation works on login");
    }

    @Test(priority = 4)
    public void testRegisterLinkOnLoginPage() {
        System.out.println("\n🧪 Test 8: Register link exists on login page");
        navigateTo("/login");
        
        // Find register link
        boolean hasRegisterLink = driver.findElements(By.cssSelector("a[href*='register']")).size() > 0 ||
                                  driver.getPageSource().toLowerCase().contains("register") ||
                                  driver.getPageSource().toLowerCase().contains("sign up");
        
        Assert.assertTrue(hasRegisterLink, "Login page should have a link/reference to register");
        
        System.out.println("✅ Test 8 PASSED: Register link/reference found on login page");
    }
}

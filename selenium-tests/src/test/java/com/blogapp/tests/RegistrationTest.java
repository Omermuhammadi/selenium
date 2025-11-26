package com.blogapp.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.UUID;

/**
 * Test cases for user registration functionality.
 * Tests: Registration page load, successful registration, validation errors
 */
public class RegistrationTest extends BaseTest {

    private String generateUniqueUsername() {
        return "testuser_" + UUID.randomUUID().toString().substring(0, 8);
    }

    private String generateUniqueEmail() {
        return "test_" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
    }

    @Test(priority = 1)
    public void testRegistrationPageLoads() {
        System.out.println("\n🧪 Test 1: Registration page loads correctly");
        navigateTo("/register");
        
        String pageSource = driver.getPageSource().toLowerCase();
        boolean hasRegisterContent = pageSource.contains("register") || 
                                     pageSource.contains("sign up") ||
                                     pageSource.contains("create account");
        
        Assert.assertTrue(hasRegisterContent, "Registration page should load with register content");
        
        // Check for registration form elements
        boolean hasUsernameField = driver.findElements(By.name("username")).size() > 0 ||
                                   driver.findElements(By.id("username")).size() > 0;
        
        Assert.assertTrue(hasUsernameField, "Username field should be present");
        
        System.out.println("✅ Test 1 PASSED: Registration page loads correctly");
    }

    @Test(priority = 2)
    public void testRegistrationFormHasAllFields() {
        System.out.println("\n🧪 Test 2: Registration form has all required fields");
        navigateTo("/register");
        
        // Check for username field
        boolean hasUsername = driver.findElements(By.name("username")).size() > 0;
        Assert.assertTrue(hasUsername, "Should have username field");
        
        // Check for email field
        boolean hasEmail = driver.findElements(By.name("email")).size() > 0;
        Assert.assertTrue(hasEmail, "Should have email field");
        
        // Check for password field
        boolean hasPassword = driver.findElements(By.name("password")).size() > 0;
        Assert.assertTrue(hasPassword, "Should have password field");
        
        // Check for submit button
        boolean hasSubmit = driver.findElements(By.cssSelector("button[type='submit']")).size() > 0 ||
                           driver.findElements(By.cssSelector("input[type='submit']")).size() > 0;
        Assert.assertTrue(hasSubmit, "Should have submit button");
        
        System.out.println("✅ Test 2 PASSED: Registration form has all required fields");
    }

    @Test(priority = 3)
    public void testSuccessfulRegistration() {
        System.out.println("\n🧪 Test 3: Successful user registration");
        navigateTo("/register");
        
        String username = generateUniqueUsername();
        String email = generateUniqueEmail();
        String password = "TestPass123!";
        
        // Fill registration form
        WebElement usernameField = driver.findElement(By.name("username"));
        WebElement emailField = driver.findElement(By.name("email"));
        WebElement passwordField = driver.findElement(By.name("password"));
        
        usernameField.clear();
        usernameField.sendKeys(username);
        emailField.clear();
        emailField.sendKeys(email);
        passwordField.clear();
        passwordField.sendKeys(password);
        
        // Submit form
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();
        
        // Wait for page to process
        sleep(3000);
        
        // Verify registration success (redirected to login or shows success)
        String currentUrl = driver.getCurrentUrl();
        String pageSource = driver.getPageSource().toLowerCase();
        
        boolean registrationSuccess = currentUrl.contains("/login") || 
                                     currentUrl.equals(BASE_URL + "/") ||
                                     pageSource.contains("success") ||
                                     pageSource.contains("registered") ||
                                     !currentUrl.contains("/register");
        
        Assert.assertTrue(registrationSuccess, "Should redirect after successful registration");
        
        System.out.println("✅ Test 3 PASSED: User registered successfully - " + username);
    }

    @Test(priority = 4)
    public void testRegistrationWithEmptyFields() {
        System.out.println("\n🧪 Test 4: Registration with empty fields shows validation");
        navigateTo("/register");
        
        // Try to submit empty form by clicking submit directly
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();
        
        sleep(1000);
        
        // Check that we're still on registration page or validation message shown
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("/register"), 
                         "Should stay on registration page with empty fields");
        
        System.out.println("✅ Test 4 PASSED: Empty field validation works");
    }
}

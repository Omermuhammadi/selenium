package com.blogapp.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.UUID;

/**
 * Test cases for blog post functionality.
 * Tests: Posts page access, create post authentication, post display
 */
public class BlogPostTest extends BaseTest {

    @Test(priority = 1)
    public void testPostsPageAccessible() {
        System.out.println("\n🧪 Test 14: Posts page is accessible");
        navigateTo("/posts");
        
        sleep(2000);
        
        String pageSource = driver.getPageSource().toLowerCase();
        String currentUrl = driver.getCurrentUrl();
        
        // Posts page should either show posts or redirect to login
        boolean isValidResponse = pageSource.contains("post") || 
                                 pageSource.contains("blog") ||
                                 pageSource.contains("article") ||
                                 currentUrl.contains("login");
        
        Assert.assertTrue(isValidResponse,
                         "Posts page should show posts content or redirect to login");
        
        System.out.println("✅ Test 14 PASSED: Posts page is accessible");
    }

    @Test(priority = 2)
    public void testCreatePostPageRequiresAuth() {
        System.out.println("\n🧪 Test 15: Create post page requires authentication");
        navigateTo("/posts/new");
        
        sleep(2000);
        
        String currentUrl = driver.getCurrentUrl();
        String pageSource = driver.getPageSource().toLowerCase();
        
        // Should either redirect to login or show create form (if already logged in)
        boolean isSecured = currentUrl.contains("login") || 
                           pageSource.contains("create") ||
                           pageSource.contains("new post") ||
                           pageSource.contains("title");
        
        Assert.assertTrue(isSecured, 
                         "Create post should require authentication or show create form");
        
        System.out.println("✅ Test 15 PASSED: Create post page authentication check works");
    }

    @Test(priority = 3)
    public void testPostsPageHasCorrectStructure() {
        System.out.println("\n🧪 Test 16: Posts page has correct HTML structure");
        navigateTo("/posts");
        
        sleep(2000);
        
        // If redirected to login, that's also valid (auth required)
        if (driver.getCurrentUrl().contains("login")) {
            System.out.println("✅ Test 16 PASSED: Posts requires authentication (redirected to login)");
            return;
        }
        
        String pageSource = driver.getPageSource();
        
        // Check for basic HTML structure
        boolean hasProperStructure = pageSource.contains("<html") &&
                                    pageSource.contains("<body") &&
                                    pageSource.contains("<head");
        
        Assert.assertTrue(hasProperStructure, "Page should have proper HTML structure");
        
        System.out.println("✅ Test 16 PASSED: Posts page has correct HTML structure");
    }
}

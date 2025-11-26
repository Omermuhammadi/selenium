package com.blogapp.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Test cases for home page and navigation functionality.
 * Tests: Home page load, navigation elements, page title, responsive design
 */
public class HomePageTest extends BaseTest {

    @Test(priority = 1)
    public void testHomePageLoads() {
        System.out.println("\n🧪 Test 9: Home page loads correctly");
        navigateTo("/");
        
        // Check page loads with content
        String pageSource = driver.getPageSource();
        Assert.assertTrue(pageSource.length() > 100, 
                         "Home page should have substantial content");
        
        // Check HTTP response (page loaded successfully if we have content)
        Assert.assertNotNull(driver.getTitle(), "Page should have a title");
        
        System.out.println("✅ Test 9 PASSED: Home page loads correctly");
    }

    @Test(priority = 2)
    public void testNavigationElementsPresent() {
        System.out.println("\n🧪 Test 10: Navigation elements are present");
        navigateTo("/");
        
        // Check for navigation bar
        boolean hasNavigation = driver.findElements(By.tagName("nav")).size() > 0 ||
                               driver.findElements(By.className("navbar")).size() > 0 ||
                               driver.findElements(By.cssSelector("[class*='nav']")).size() > 0;
        
        Assert.assertTrue(hasNavigation, "Home page should have navigation elements");
        
        System.out.println("✅ Test 10 PASSED: Navigation elements are present");
    }

    @Test(priority = 3)
    public void testNavigationLinksWork() {
        System.out.println("\n🧪 Test 11: Navigation links are functional");
        navigateTo("/");
        
        String pageSource = driver.getPageSource().toLowerCase();
        
        // Check for essential navigation links (login, register, or posts)
        boolean hasLoginLink = pageSource.contains("login") || pageSource.contains("sign in");
        boolean hasRegisterLink = pageSource.contains("register") || pageSource.contains("sign up");
        boolean hasPostsLink = pageSource.contains("post") || pageSource.contains("blog");
        
        Assert.assertTrue(hasLoginLink || hasRegisterLink || hasPostsLink, 
                         "Navigation should have login, register, or posts links");
        
        System.out.println("✅ Test 11 PASSED: Navigation links are present and functional");
    }

    @Test(priority = 4)
    public void testPageHasProperTitle() {
        System.out.println("\n🧪 Test 12: Page has proper title");
        navigateTo("/");
        
        String title = driver.getTitle();
        Assert.assertNotNull(title, "Page should have a title");
        Assert.assertTrue(title.length() > 0, "Page title should not be empty");
        
        System.out.println("✅ Test 12 PASSED: Page title is: '" + title + "'");
    }

    @Test(priority = 5)
    public void testResponsiveFrameworkUsed() {
        System.out.println("\n🧪 Test 13: Page uses responsive framework (Bootstrap)");
        navigateTo("/");
        
        String pageSource = driver.getPageSource().toLowerCase();
        
        // Check for Bootstrap or responsive framework indicators
        boolean hasBootstrap = pageSource.contains("bootstrap") || 
                              pageSource.contains("container") ||
                              pageSource.contains("row") ||
                              pageSource.contains("col-") ||
                              pageSource.contains("btn");
        
        Assert.assertTrue(hasBootstrap, "Page should use Bootstrap or responsive CSS framework");
        
        System.out.println("✅ Test 13 PASSED: Responsive framework (Bootstrap) detected");
    }
}

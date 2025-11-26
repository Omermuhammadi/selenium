package com.blogapp.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.time.Duration;

/**
 * Base test class providing common setup and teardown for all Selenium tests.
 * Configures headless Chrome browser for CI/CD pipeline compatibility.
 */
public class BaseTest {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected static String BASE_URL;

    @BeforeSuite
    public void setupSuite() {
        // Get base URL from system property or use default
        BASE_URL = System.getProperty("baseUrl", "http://localhost:3000");
        System.out.println("========================================");
        System.out.println("🌐 BlogApp Selenium Test Suite");
        System.out.println("🔗 Testing against: " + BASE_URL);
        System.out.println("========================================");
    }

    @BeforeMethod
    public void setUp() {
        // Setup ChromeDriver using WebDriverManager
        WebDriverManager.chromedriver().setup();

        // Configure Chrome options for headless mode (required for Jenkins/EC2)
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-infobars");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * Navigate to a specific path relative to the base URL
     * @param path The path to navigate to (e.g., "/login", "/register")
     */
    protected void navigateTo(String path) {
        driver.get(BASE_URL + path);
    }

    /**
     * Sleep for specified milliseconds (use sparingly)
     * @param milliseconds Time to sleep
     */
    protected void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

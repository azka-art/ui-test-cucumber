package com.automation.tests.driver;

import com.automation.tests.config.ConfigManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class DriverFactory {
    private static final Logger logger = LoggerFactory.getLogger(DriverFactory.class);
    private static final ConfigManager config = ConfigManager.getInstance();
    
    private DriverFactory() {
        // Private constructor to prevent instantiation
    }
    
    public static WebDriver createDriver() {
        String browser = config.getProperty("browser", "chrome").toLowerCase();
        boolean isHeadless = config.getBooleanProperty("headless", false);
        boolean useGrid = config.getBooleanProperty("grid.enabled", false);
        
        logger.info("Creating WebDriver instance for browser: {} (headless: {}, grid: {})", 
                    browser, isHeadless, useGrid);
        
        try {
            WebDriver driver;
            
            if (useGrid) {
                driver = createRemoteDriver(browser, isHeadless);
            } else {
                driver = createLocalDriver(browser, isHeadless);
            }
            
            // Configure timeouts
            int timeout = config.getIntProperty("timeout", 10);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeout));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(timeout * 3));
            driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(timeout * 2));
            driver.manage().window().maximize();
            
            logger.info("WebDriver initialized successfully");
            return driver;
            
        } catch (Exception e) {
            logger.error("Failed to initialize WebDriver: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to initialize WebDriver", e);
        }
    }
    
    private static WebDriver createLocalDriver(String browser, boolean isHeadless) {
        switch (browser) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (isHeadless) { firefoxOptions.addArguments("--headless"); }
                return new FirefoxDriver(firefoxOptions);
                
            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                if (isHeadless) {
                    edgeOptions.addArguments("--headless");
                }
                return new EdgeDriver(edgeOptions);
                
            case "safari":
                return new SafariDriver();
                
            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                if (isHeadless) {
                    chromeOptions.addArguments("--headless=new");
                }
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                return new ChromeDriver(chromeOptions);
        }
    }
    
    private static WebDriver createRemoteDriver(String browser, boolean isHeadless) {
        String gridUrl = config.getProperty("grid.url", "http://localhost:4444/wd/hub");
        logger.info("Using Selenium Grid at: {}", gridUrl);
        
        try {
            switch (browser) {
                case "firefox":
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    if (isHeadless) { firefoxOptions.addArguments("--headless"); }
                    return new RemoteWebDriver(new URL(gridUrl), firefoxOptions);
                    
                case "edge":
                    EdgeOptions edgeOptions = new EdgeOptions();
                    if (isHeadless) {
                        edgeOptions.addArguments("--headless");
                    }
                    return new RemoteWebDriver(new URL(gridUrl), edgeOptions);
                    
                case "chrome":
                default:
                    ChromeOptions chromeOptions = new ChromeOptions();
                    if (isHeadless) {
                        chromeOptions.addArguments("--headless=new");
                    }
                    chromeOptions.addArguments("--disable-gpu");
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                    return new RemoteWebDriver(new URL(gridUrl), chromeOptions);
            }
        } catch (MalformedURLException e) {
            logger.error("Invalid Grid URL: {}", gridUrl, e);
            throw new RuntimeException("Invalid Grid URL: " + gridUrl, e);
        }
    }
}

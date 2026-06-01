package com.example.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class BaseTest {

    protected WebDriver driver;

    @BeforeEach
    void setUp() {
        String browserProperty = System.getProperty("browser");
        String browser = (browserProperty == null || browserProperty.isBlank())
                ? "chrome"
                : browserProperty.trim().toLowerCase();
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "true"));
        String remoteUrl = resolveRemoteUrl();

        switch (browser) {
            case "edge" -> {
                EdgeOptions options = new EdgeOptions();
                configureHeadlessOptions(options, headless);
                driver = remoteUrl == null
                        ? createLocalEdgeDriver(options)
                        : createRemoteDriver(remoteUrl, options);
            }
            case "chrome" -> {
                ChromeOptions options = new ChromeOptions();
                configureHeadlessOptions(options, headless);
                options.addArguments("--window-size=1920,1080");
                driver = remoteUrl == null
                        ? createLocalChromeDriver(options)
                        : createRemoteDriver(remoteUrl, options);
            }
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser + ". Use chrome or edge.");
        }
    }

    private String resolveRemoteUrl() {
        String remoteUrl = System.getProperty("remoteUrl");
        if (remoteUrl == null || remoteUrl.isBlank()) {
            remoteUrl = System.getenv("SELENIUM_REMOTE_URL");
        }
        return (remoteUrl == null || remoteUrl.isBlank()) ? null : remoteUrl.trim();
    }

    private void configureHeadlessOptions(ChromeOptions options, boolean headless) {
        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--no-sandbox");
        }
    }

    private void configureHeadlessOptions(EdgeOptions options, boolean headless) {
        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--no-sandbox");
        }
    }

    private WebDriver createLocalChromeDriver(ChromeOptions options) {
        WebDriverManager.chromedriver().setup();
        return new ChromeDriver(options);
    }

    private WebDriver createLocalEdgeDriver(EdgeOptions options) {
        WebDriverManager.edgedriver().setup();
        return new EdgeDriver(options);
    }

    private WebDriver createRemoteDriver(String remoteUrl, ChromeOptions options) {
        try {
            return new RemoteWebDriver(new URL(remoteUrl), options);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid remoteUrl: " + remoteUrl, e);
        }
    }

    private WebDriver createRemoteDriver(String remoteUrl, EdgeOptions options) {
        try {
            return new RemoteWebDriver(new URL(remoteUrl), options);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid remoteUrl: " + remoteUrl, e);
        }
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}

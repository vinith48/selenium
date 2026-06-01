package com.example.selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimpleSeleniumTests extends BaseTest {

    private static final String FORM_URL = "https://www.selenium.dev/selenium/web/web-form.html";

    @Test
    void shouldOpenPageAndVerifyTitle() {
        driver.get(FORM_URL);

        assertEquals("Web form", driver.getTitle());
    }

    @Test
    void shouldSubmitFormSuccessfully() {
        driver.get(FORM_URL);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement textInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("my-text")));
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));

        textInput.sendKeys("Hello Selenium");
        submitButton.click();

        wait.until(ExpectedConditions.urlContains("submitted-form.html"));
        WebElement message = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("message")));
        assertTrue(message.getText().contains("Received!"));
    }
}

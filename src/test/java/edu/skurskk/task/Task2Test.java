package edu.skurskk.task;

import edu.skurskk.framework.utils.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;


public class Task2Test {
    private static final String URL = "https://pl.wikipedia.org/wiki/Wiki";
    private static final int TIMEOUT = 60;
    private WebDriver webDriver;
    private Wait<WebDriver> wait;

    private static final By LOCATOR_LANG_BUTTON = By.id("p-lang-btn-checkbox");
    private static final By LOCATOR_LANG_LIST = By.className("autonym");


    @BeforeTest
    public void setup() {
        DriverFactory driverFactory = new DriverFactory();
        webDriver = driverFactory.initDriver();
        wait = new WebDriverWait(webDriver, Duration.ofSeconds(TIMEOUT));
        webDriver.get(URL);
    }

    @AfterTest
    public void afterTest() {
        webDriver.quit();
    }

    @Test
    public void task2Test() {
        wait.until(presenceOfElementLocated(LOCATOR_LANG_BUTTON));
        WebElement langButton = webDriver.findElement(LOCATOR_LANG_BUTTON);
        langButton.click();

        wait.until(presenceOfAllElementsLocatedBy(LOCATOR_LANG_LIST));
        List<WebElement> langs = webDriver.findElements(LOCATOR_LANG_LIST);

        langs.forEach(el -> {
            if (el.getText().equals("English")) {
                System.out.println(el.getText() + " " + el.getAttribute("href"));
            } else {
                System.out.println(el.getText());
            }
        });
    }
}

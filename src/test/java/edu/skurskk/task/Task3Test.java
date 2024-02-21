package edu.skurskk.task;

import edu.skurskk.framework.utils.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.numberOfWindowsToBe;
import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

public class Task3Test {
    private static final String URL = "https://www.google.com/";
    private static final int TIMEOUT_PAGE_LOAD_SEC = 30;
    private WebDriver webDriver;
    private Wait<WebDriver> wait;

    private static final String SEARCHED_PHRASE = "HTML select tag - W3Schools";
    private static final String W3SCHOOL_URL = "https://www.w3schools.com/tags/tag_select.asp";
    private static final String W3SCHOOL_TITLE = "W3Schools Tryit Editor";

    private static final By LOCATOR_GOOGLE_ACCEPT_COOKIE = By.id("L2AGLb");
    private static final By LOCATOR_GOOGLE_INPUT = By.id("APjFqb");
    private static final By LOCATOR_GOOGLE_LUCKY_BUTTON = By.name("btnI");
    private static final By LOCATOR_W3SCHOOL_ACCEPT_COOKIE = By.id("accept-choices");
    private static final By LOCATOR_TRY_IT_YOURSELF = By.cssSelector(".w3-btn.w3-margin-bottom");
    private static final By LOCATOR_W3SCOOLS_H1 =
            By.xpath("//*[@class='CodeMirror-lines']//span[text()='The select element']");
    private static final By LOCATOR_IFRAME = By.id("iframeResult");
    private static final By LOCATOR_CARS_DROPDOWN = By.name("cars");


    @BeforeTest
    public void setup() {
        DriverFactory driverFactory = new DriverFactory();
        webDriver = driverFactory.initDriver();
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(webDriver, Duration.ofSeconds(TIMEOUT_PAGE_LOAD_SEC));
        webDriver.get(URL);
    }

    @AfterTest
    public void afterTest() {
        webDriver.quit();
    }

    @Test
    public void task3Test(){
        WebElement googleAcceptCookieButton = webDriver.findElement(LOCATOR_GOOGLE_ACCEPT_COOKIE);
        googleAcceptCookieButton.click();
        WebElement googleInput = webDriver.findElement(LOCATOR_GOOGLE_INPUT);
        googleInput.sendKeys(SEARCHED_PHRASE);

        wait.until(elementToBeClickable(LOCATOR_GOOGLE_LUCKY_BUTTON)).click();

        String actualUrl = webDriver.getCurrentUrl();
        if (!actualUrl.equals(W3SCHOOL_URL)) {
            webDriver.get(W3SCHOOL_URL);
        }

        WebElement w3SchoolsAcceptsCookiesButton = webDriver.findElement(LOCATOR_W3SCHOOL_ACCEPT_COOKIE);
        w3SchoolsAcceptsCookiesButton.click();

        String orgWindow = webDriver.getWindowHandle();

        WebElement tryItYourselfButton = webDriver.findElement(LOCATOR_TRY_IT_YOURSELF);
        tryItYourselfButton.click();

        switchToNewWindow(orgWindow);
        printW3SchoolExampleHeader();

        WebElement iframe = webDriver.findElement(LOCATOR_IFRAME);
        webDriver.switchTo().frame(iframe);

        selectOpelFromDropDownList();

        webDriver.switchTo().defaultContent();
    }

    private void switchToNewWindow(String orginalWindow) {
        wait.until(numberOfWindowsToBe(2));

        for (String windowHandle : webDriver.getWindowHandles()) {
            if (!orginalWindow.contentEquals(windowHandle)) {
                webDriver.switchTo().window(windowHandle);
                break;
            }
        }
        wait.until(titleIs(W3SCHOOL_TITLE));
    }

    private void selectOpelFromDropDownList() {
        WebElement carsDropdownList = webDriver.findElement(LOCATOR_CARS_DROPDOWN);
        Select carsDropdown = new Select(carsDropdownList);
        carsDropdown.selectByValue("opel");
        WebElement opelOption = carsDropdown.getFirstSelectedOption();

        System.out.println(opelOption.getText() + ", " + opelOption.getAttribute("value"));
    }

    private void printW3SchoolExampleHeader() {
        WebElement w3SchoolExampleH1 = webDriver.findElement(LOCATOR_W3SCOOLS_H1);
        System.out.println(w3SchoolExampleH1.getText());
    }

}

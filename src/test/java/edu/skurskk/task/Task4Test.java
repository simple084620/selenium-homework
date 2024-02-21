package edu.skurskk.task;

import edu.skurskk.framework.utils.DriverFactory;
import edu.skurskk.model.YTTile;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

public class Task4Test {
    private static final String URL = "https://www.youtube.com/";
    private static final int TILES_LIMIT = 12;
    private static final int TIMEOUT_PAGE_LOAD_SEC = 30;
    private WebDriver webDriver;
    private Wait<WebDriver> wait;

    private static final By LOCATOR_ACCEPT_COOKIES =
            By.xpath("//*[contains(@aria-label, 'Accept')]//div[@class='yt-spec-touch-feedback-shape__fill']");
    private static final By LOCATOR_TILES = By.xpath("//*[@class='style-scope ytd-rich-grid-row']//div[@id='content']");
    private static final By LOCATOR_IS_LIVE = By.xpath(".//*[contains(text(), 'LIVE')]");
    private static final By LOCATOR_LENGTH = By.xpath(".//div[@id='time-status']");
    private static final By LOCATOR_TITLE = By.xpath(".//*[@id='video-title']");
    private static final By LOCATOR_CHANNEL = By.xpath(".//div[@id='text-container']");
    private static final By LOCATOR_TIME_STATUS = By.id("time-status");

    @BeforeTest
    public void setup() {
        DriverFactory driverFactory = new DriverFactory();
        webDriver = driverFactory.initDriver();
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        wait = new WebDriverWait(webDriver, Duration.ofSeconds(TIMEOUT_PAGE_LOAD_SEC));
        webDriver.get(URL);
    }

    @AfterTest
    public void afterTest() {
        webDriver.quit();
    }

    @Test
    public void task4Test() {
        wait.until(presenceOfElementLocated(LOCATOR_ACCEPT_COOKIES)).click();
        wait.until(presenceOfElementLocated(LOCATOR_TIME_STATUS));

        List<WebElement> ytTiles = webDriver.findElements(LOCATOR_TILES)
                .stream()
                .limit(TILES_LIMIT)
                .collect(Collectors.toList());

        List<YTTile> ytTileList = getYTTiles(ytTiles);

        printVideosLength(ytTileList);
    }

    private List<YTTile> getYTTiles(List<WebElement> tilesWebElements) {
        return tilesWebElements.stream()
                .map(tile -> {
                    String length;
                    if (isElementPresent(tile, LOCATOR_IS_LIVE)) {
                        length = tile.findElement(LOCATOR_IS_LIVE).getText();
                    } else {
                        length = tile.findElement(LOCATOR_LENGTH).getText();
                    }

                    String title = tile.findElement(LOCATOR_TITLE).getText();
                    String channel = tile.findElement(LOCATOR_CHANNEL).getText();

                    return new YTTile(title, channel, length);
                }).collect(Collectors.toList());
    }

    private void printVideosLength(List<YTTile> tiles) {
        tiles.stream()
                .filter(t -> !t.getLength().equals("LIVE"))
                .forEach(t -> System.out.println(t.getLength()));
    }

    private boolean isElementPresent(WebElement parent, By by) {
        return !parent.findElements(by).isEmpty();
    }

    private void waitForPageToLoad() {
        ExpectedCondition<Boolean> pageLoadCondition =
                wd -> "complete"
                        .equals(((JavascriptExecutor) wd)
                                .executeScript("return document.readyState"));
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(TIMEOUT_PAGE_LOAD_SEC));
        wait.until(pageLoadCondition);
    }
}

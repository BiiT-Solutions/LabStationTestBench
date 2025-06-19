package com.biit.labstation;


import com.biit.labstation.logger.LabStationLogger;
import io.github.bonigarcia.wdm.WebDriverManager;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomChromeDriver {

    private static final boolean DESTROY_DRIVER = true;
    private static final Integer WIDTH = 1920;
    private static final Integer HEIGHT = 1080;
    private static final Duration WAIT_TIMEOUT_SECS = Duration.ofSeconds(3);

    private String language = "en-GB";

    private WebDriver driver;

    private WebDriverWait webDriverWait;

    @Value("${headless.mode:true}")
    private boolean headlessMode;

    private ChromeOptions getChromeOptions(boolean headless) {
        final ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("start-maximized");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-search-engine-choice-screen");
        chromeOptions.addArguments("--lang=" + language);
        //Previous line not working, adding a workaround.
        final Map<String, Object> prefs = new HashMap<>();
        prefs.put("intl.accept_languages", language);
        chromeOptions.setExperimentalOption("prefs", prefs);
        if (headless) {
            chromeOptions.addArguments("--headless=new");
        }
        chromeOptions.setAcceptInsecureCerts(true);
//        if (!headless) {
//            chromeOptions.setExperimentalOption("detach", true);
//        }
        return chromeOptions;
    }

    @PostConstruct
    public WebDriver getDriver() {
        if (driver == null) {
            LabStationLogger.debug(this.getClass().getName(), "Starting webdriver...");
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver(getChromeOptions(isHeadless()));
            driver.manage().window().setPosition(new Point(0, 0));
            driver.manage().window().setSize(new Dimension(WIDTH, HEIGHT));
            webDriverWait = new WebDriverWait(driver, WAIT_TIMEOUT_SECS);
        }
        return driver;
    }

    @PreDestroy
    public void closeDriver() {

        if (DESTROY_DRIVER) {
            try {
                if (driver != null) {
                    LabStationLogger.debug(this.getClass().getName(), "Closing webdriver...");
                    getDriver().close();
                    getDriver().quit();
                    driver = null;
                }
            } catch (NullPointerException npe) {
                // Already destroyed.
            }
        }
    }

    public boolean isHeadless() {
        return headlessMode;
    }

    public WebDriverWait getWebDriverWait() {
        return webDriverWait;
    }

    public WebElement findElement(By field) {
        return getDriver().findElement(field);
    }

    public WebElement findElementWaiting(By field) {
        return getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated(field));
    }

    public WebElement findElementWaiting(By parent, By field) {
        getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated(parent));
        return getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated(field));
    }
}

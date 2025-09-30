package com.biit.labstation;

import com.biit.labstation.components.Login;
import com.biit.labstation.components.Popup;
import com.biit.labstation.components.PopupId;
import com.biit.labstation.exceptions.ElementNotFoundAsExpectedException;
import com.biit.labstation.logger.LabStationLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.time.Duration;

import static org.awaitility.Awaitility.await;

public abstract class ToolTest {
    public static final int WAITING_TIME = 500;
    public static final int ONE_SECOND_WAITING_TIME = 2000;
    public static final int THREE_SECONDs_WAITING_TIME = 3000;
    public static final int FIVE_SECONDS_WAITING_TIME = 5000;
    protected static final int WAITING_TIME_SECONDS = 3;
    protected static final int LONG_WAITING_TIME_SECONDS = 15;
    private static final int WAITING_TO_ACCESS_BROWSER = 2000;

    private final CustomChromeDriver customChromeDriver;
    private final Login login;
    private final Popup popup;

    protected ToolTest(CustomChromeDriver customChromeDriver, Login login, Popup popup) {
        this.customChromeDriver = customChromeDriver;
        this.login = login;
        this.popup = popup;
    }

    public CustomChromeDriver getCustomChromeDriver() {
        return customChromeDriver;
    }

    protected void waitAndExecute(Runnable operation) {
        await().atMost(Duration.ofSeconds(WAITING_TIME_SECONDS)).until(() -> {
            try {
                operation.run();
                return true;
            } catch (Exception e) {
                return false;
            }
        });
    }

    public static void waitComponent() {
        waitComponent(WAITING_TIME);
    }

    public static void waitComponentOneSecond() {
        waitComponent(ONE_SECOND_WAITING_TIME);
    }

    public static void waitComponentThreeSecond() {
        waitComponent(THREE_SECONDs_WAITING_TIME);
    }

    public static void waitComponentFiveSecond() {
        waitComponent(FIVE_SECONDS_WAITING_TIME);
    }

    public static void waitComponent(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            //Ignore
            Thread.currentThread().interrupt();
        }
    }

    public abstract void access();

    public void login(String username, String password) {
        access();
        waitComponent();
        try {
            login.acceptCookies();
        } catch (Exception e) {
            //Ignored.
        }
        waitComponent();
        login.logIn(username, password);
        waitComponentOneSecond();
    }

    public abstract void logout();

    public void access(String serverDomain, String context) {
        try {
            final String requestedUrl = serverDomain + context;
            LabStationLogger.debug(this.getClass().getName(), "Accessing to URL '{}'.", requestedUrl);
            customChromeDriver.getDriver().get(requestedUrl);
            waitComponent(WAITING_TO_ACCESS_BROWSER);
            final String currentUrl = customChromeDriver.getDriver().getCurrentUrl();
            if (currentUrl == null || !currentUrl.startsWith(requestedUrl)) {
                LabStationLogger.info(this.getClass(), "Browser is not ready yet! Current URL is '{}'. Requested is '{}'. Waiting...",
                        currentUrl, requestedUrl);
                access(serverDomain, context);
            }
            LabStationLogger.info(this.getClass(), "Opened '{}'.", currentUrl);
        } catch (Exception e) {
            LabStationLogger.errorMessage(this.getClass(), e);
        }
    }

    public void signUp(String username, String password, String name, String lastname, String email) {
        try {
            login.acceptCookies();
        } catch (Exception e) {
            //Ignored.
        }
        popup.close(PopupId.LOGIN_WARNING);
        login.signUp(username, password, name, lastname, email);
    }

    public void join(String title) {
        final WebElement webElement = getCustomChromeDriver().findElementWaiting(By.id("join-button"));
        if (!webElement.getText().contains(title.toUpperCase())) {
            throw new ElementNotFoundAsExpectedException("Button is not for title '" + title + "',");
        }
        webElement.click();
    }
}

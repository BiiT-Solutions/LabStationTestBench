package com.biit.labstation.components;

import com.biit.labstation.CustomChromeDriver;
import com.biit.labstation.ToolTest;
import com.biit.labstation.logger.ComponentLogger;
import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@Component
public class SnackBar {
    public static final String REQUEST_SUCCESSFUL = "Your request has been completed successfully.";
    public static final String USER_CREATED = "User has been created successfully.";
    public static final String LOGGED_OUT = "Your account was logged out successfully.";
    public static final String ACTION_NOT_ALLOWED = "This action is not allowed.";
    public static final String ACCESS_DENIED = "Access denied.";
    public static final String USER_ALREADY_EXISTS = "The user already exists.";
    public static final String EMAIL_IN_USE = "The email is already in use.";
    public static final String REQUEST_FAILED = "Your request failed. Please, try again later.";

    protected static final int WAITING_TIME_SECONDS = 5;

    private final CustomChromeDriver customChromeDriver;

    public SnackBar(CustomChromeDriver customChromeDriver) {
        this.customChromeDriver = customChromeDriver;
    }

    public String getMessageType() {
        final String attribute = customChromeDriver.findElementWaiting(By.id("snackbar-canvas"), By.id("biit-notification")).getAttribute("class");
        if (attribute != null) {
            return attribute.replace("notification-canvas", "").trim();
        }
        return null;
    }

    public String getMessage() {
        await().atMost(Duration.ofSeconds(WAITING_TIME_SECONDS)).and().with().pollDelay(1, TimeUnit.SECONDS).until(() -> {
            try {
                //Wait until snackbar is visible.
                customChromeDriver.findElementWaiting(By.id("snackbar-canvas")).findElement(By.id("biit-notification"));
                return true;
            } catch (Exception e) {
                return false;
            }
        });
        final String text = customChromeDriver.findElementWaiting(By.id("snackbar-canvas"), By.id("biit-notification"))
                .findElement(By.id("message")).getText();
        if (!text.isBlank()) {
            ComponentLogger.debug(this.getClass().getName(), "SnackBar Text: '{}'.", text);
        }
        return text;
    }

    private void close() {
        customChromeDriver.findElement(By.id("snackbar-canvas")).findElement(By.id("biit-notification")).click();
    }

    public void checkMessage(String type, String message) {
        await().atMost(Duration.ofSeconds(WAITING_TIME_SECONDS)).and().with().pollDelay(ToolTest.WAITING_TIME, TimeUnit.MILLISECONDS).until(() -> {
            try {
                return Objects.equals(getMessageType(), type) && Objects.equals(getMessage(), message);
            } catch (Exception e) {
                return false;
            }
        });
        close();
    }
}

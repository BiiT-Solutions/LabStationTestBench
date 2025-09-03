package com.biit.labstation.components;

import com.biit.labstation.CustomChromeDriver;
import com.biit.labstation.logger.ComponentLogger;
import com.biit.labstation.logger.LabStationLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
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
    public static final String NO_ASSIGNED_PROFILES = "Oops! It seems there are no assigned candidates for this profile.";
    public static final String DATA_IS_INVALID = "Provided data are invalid!";
    public static final String CANNOT_SELECT_MORE_THAN_TEN = "You cannot select more than 10 cards. Please, deselect one to select another.";
    public static final String USERNAME_ALREADY_EXISTS = "The user already exists.";
    public static final String DATA_CONFLICTS = "Inserted data conflicts with already existing data on database.";

    public enum Type {
        REGULAR,
        WARNING,
        ERROR;
    }

    protected static final int WAITING_TIME_SECONDS = 5;
    protected static final int SNACKBAR_WAITING_TIME = 250;

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

    public void closeLatest() {
        final List<WebElement> notifications = customChromeDriver.findElement(By.id("snackbar-canvas")).findElements(By.id("biit-notification"));
        if (notifications.size() > 1) {
            LabStationLogger.info(this.getClass().getName(), "Notifications found: '{}'.", notifications.size());
        }
        notifications.get(notifications.size() - 1).click();
    }

    public void checkMessage(SnackBar.Type type, String message) {
        checkMessage(type.toString().toLowerCase(), message);
    }

    private void checkMessage(String type, String message) {
        await().atMost(Duration.ofSeconds(WAITING_TIME_SECONDS)).and().with().pollDelay(SNACKBAR_WAITING_TIME, TimeUnit.MILLISECONDS).until(() -> {
            try {
                LabStationLogger.debug(this.getClass().getName(), "Comparing messages: '{}' ({}) with '{}' ({}).",
                        message, type, getMessage(), getMessageType());
                if (Objects.equals(getMessageType(), type) && Objects.equals(getMessage(), message)) {
                    closeLatest();
                    LabStationLogger.debug(this.getClass().getName(), "Message found!");
                    return true;
                }
            } catch (Exception e) {
                //Not present yet.
            }
            return false;
        });
    }
}

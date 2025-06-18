package com.biit.labstation.components;

import com.biit.labstation.CustomChromeDriver;
import com.biit.labstation.logger.ComponentLogger;
import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@Component
public class SnackBar {
    protected static final int WAITING_TIME_SECONDS = 4;

    private final CustomChromeDriver customChromeDriver;

    public SnackBar(CustomChromeDriver customChromeDriver) {
        this.customChromeDriver = customChromeDriver;
    }

    public String getMessageType() {
        final String attribute = customChromeDriver.findElementWaiting(By.id("snackbar-canvas"), By.id("biit-notification")).getAttribute("class");
        if (attribute != null) {
            final String type = attribute.replace("notification-canvas", "").trim();
            ComponentLogger.debug(this.getClass().getName(), "SnackBar Type: '{}'.", type);
            return type;
        }
        return null;
    }

    public String getMessage() {
        await().atMost(Duration.ofSeconds(WAITING_TIME_SECONDS)).and().with().pollDelay(1, TimeUnit.SECONDS).until(() -> {
            try {
                //Wait until snackbar is visible.
                customChromeDriver.findElementWaiting(By.id("snackbar-canvas"));
                return true;
            } catch (Exception e) {
                return false;
            }
        });
        final String text = customChromeDriver.findElementWaiting(By.id("snackbar-canvas")).findElement(By.id("biit-notification"))
                .findElement(By.id("message")).getText();
        if (text != null && !text.isBlank()) {
            ComponentLogger.debug(this.getClass().getName(), "SnackBar Text: '{}'.", text);
        }
        return text;
    }

    public void close() {
        customChromeDriver.findElementWaiting(By.id("snackbar-canvas"), By.id("biit-notification")).click();
    }

    public void checkMessage(String type, String message) {
        if (!Objects.equals(getMessageType(), type)) {
            throw new AssertionError("Message type mismatch. Expected: '" + type + "', Actual: '" + getMessageType() + "'.");
        }
        if (!Objects.equals(getMessage(), message)) {
            throw new AssertionError("Message content mismatch. Expected: '" + message + "', Actual: '" + getMessage() + "'.");
        }
        close();
    }
}

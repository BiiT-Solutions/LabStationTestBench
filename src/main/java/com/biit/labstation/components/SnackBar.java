package com.biit.labstation.components;

import com.biit.labstation.CustomChromeDriver;
import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

import java.time.Duration;
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
        final String attribute = customChromeDriver.findElementWaiting(By.id("snackbar-canvas")).findElement(By.id("biit-notification")).getAttribute("class");
        if (attribute != null) {
            return attribute.replace("notification-canvas", "").trim();
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
        return customChromeDriver.findElementWaiting(By.id("snackbar-canvas")).findElement(By.id("biit-notification")).findElement(By.id("message")).getText();
    }
}

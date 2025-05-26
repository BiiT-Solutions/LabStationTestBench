package com.biit.labstation.components;

import com.biit.labstation.CustomChromeDriver;
import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

@Component
public class SnackBar {

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
}

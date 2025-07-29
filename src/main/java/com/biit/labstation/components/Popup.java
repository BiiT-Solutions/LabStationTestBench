package com.biit.labstation.components;

import com.biit.labstation.CustomChromeDriver;
import com.biit.labstation.logger.ComponentLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

@Component
public class Popup {

    private final CustomChromeDriver customChromeDriver;

    public Popup(CustomChromeDriver customChromeDriver) {
        this.customChromeDriver = customChromeDriver;
    }

    public WebElement findElement(PopupId popupId, String id) {
        if (popupId != null) {
            return customChromeDriver.findElementWaiting(By.id(popupId.getId())).findElement(By.id("biit-popup"))
                    .findElement(By.id("content")).findElement(By.id(id));
        } else {
            return customChromeDriver.findElementWaiting(By.id("biit-popup")).findElement(By.id("content")).findElement(By.id(id));
        }
    }

    public void close(PopupId popupId) {
        try {
            if (popupId != null) {
                customChromeDriver.findElementWaiting(By.id(popupId.getId())).findElement(By.id("biit-popup"))
                        .findElement(By.id("header")).findElement(By.id("popup-x-button")).click();
            } else {
                customChromeDriver.findElementWaiting(By.id("biit-popup")).findElement(By.id("header")).findElement(By.id("popup-x-button")).click();
            }
            ComponentLogger.debug(this.getClass().getName(), "Closing popup '{}'.", popupId);
        } catch (Exception e) {
            //Ignore
        }
    }
}

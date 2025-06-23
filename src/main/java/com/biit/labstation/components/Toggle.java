package com.biit.labstation.components;

import com.biit.labstation.CustomChromeDriver;
import com.biit.labstation.logger.ComponentLogger;
import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

@Component
public class Toggle {

    private final CustomChromeDriver customChromeDriver;

    public Toggle(CustomChromeDriver customChromeDriver) {
        this.customChromeDriver = customChromeDriver;
    }

    public void click(String toggleId) {
        ComponentLogger.debug(this.getClass().getName(), "Selecting toggle '{}'.", toggleId);
        customChromeDriver.findElementWaiting(By.id(toggleId)).findElement(By.id("switch")).click();
    }
}
